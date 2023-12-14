package jungle.spaceship.chat.service;

import jungle.spaceship.chat.controller.dto.ChatRegisterDto;
import jungle.spaceship.chat.entity.Chat;
import jungle.spaceship.chat.entity.ChatType;
import jungle.spaceship.chat.repository.ChatRepository;
import jungle.spaceship.chat.repository.RedisMessageCache;
import jungle.spaceship.member.entity.CalendarEvent;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.repository.MemberRepository;
import jungle.spaceship.notification.FcmService;
import jungle.spaceship.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService implements DisposableBean{

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    private final RedisMessageCache messageMap;
    // 채팅 메시지 임시 저장 캐시 : 채팅방Id, 채팅 메시지
//    private static final Map<Long, Queue<Message>> messageMap = new HashMap<>();
    private final EntityManager em;
    private static final int TRANSACTION_MESSAGE_SIZE = 20; // 한번에 처리될 메시지 사이즈
    private static final int MESSAGE_PAGEABLE_SIZE = 30;    // Queue 에 임시 보관될 메시지 수
    private static final int MESSAGE_CACHE_MAX = 50;        // Write Back 패턴 중 최대 모을 수 있는 메시지 캐시


    /**
     * 메시지 보내기 및 캐시/DB 에 저장
     */
    public ChatRegisterDto sendMessage(ChatRegisterDto message, String memberEmail) {
        if(ChatType.ENTER.equals(message.getType())) {
            message.setContent(message.getSender() + "(님)이 입장하였습니다.");
        }
        saveMessage(message);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(()-> new NoSuchElementException("해당하는 사용자가 없습니다"));
        fcmService.sendFcmMessageToFamilyExcludingMe(member, NotificationType.CHAT, message.getContent());

        return message;
    }

    public void sendCalendarEventMessage(CalendarEvent calendarEvent, Member member) {
        String content = member.getNickname() + "님이 " + calendarEvent.getEventName() + "이벤트를 등록하였습니다";

        ChatRegisterDto chatRegisterDto = new ChatRegisterDto(ChatType.CALENDAR,
                member.getFamily().getChatRoom().getRoomId(),
                member.getNickname(),
                content,
                LocalDateTime.now().toString()
        );
        saveMessage(chatRegisterDto);

    }

//    private String content;
//    private String time;


    private void saveMessage(ChatRegisterDto chatRegisterDto){
        Long roomId = chatRegisterDto.getRoomId();

        Chat chat = chatRegisterDto.getNewMessage();
        System.out.println("chat = " + chat);
        // 채팅방에 캐시가 없다면 새로운 큐를 생성 및 메시지 추가 후 put(roomId, queue) 한다.
        if(!messageMap.containsKey(roomId)){
            //채팅방에 처음쓰는 글이라면 캐시가 없으므로 캐시를 생성
            Queue<Chat> q = new LinkedList<>();
            q.add(chat);
            messageMap.put(roomId, q);
        }
        else{
            Queue<Chat> mQueue = messageMap.get(roomId);
            mQueue.add(chat);
            // 캐시 쓰기 전략 (Write Back) : 큐 사이즈가 일정 크기 초과하면 일부를 db에 저장 후 큐를 갱신
            if(mQueue.size() > MESSAGE_CACHE_MAX){

                Queue<Chat> tmpQueue = new LinkedList<>();
                for (int i = 0; i < TRANSACTION_MESSAGE_SIZE; i++) {

                    tmpQueue.add(mQueue.poll());
                }
                commitMessageQueue(tmpQueue);   // 큐에서 메시지를 가져와 db에 저장
            }
            // 큐의 상태가 변경되었기 때무넹 현재 큐를 갱신
            messageMap.put(roomId, mQueue);

        }
    }

    /**
     * 채팅 메시지 페이징 처리
     */
    public List<Chat> getMessages(Long roomId) {
        // 캐시 읽기 전략 (LookAside) : 1차로 큐에 메시지가 저장되어있는지 확인 후, 없다면 db에서 조회

        // 큐에서 조회
        if(messageMap.containsKey(roomId)){
            return getMessageInCache(roomId);
        }
        // DB에서 조회
        List<Chat> chatList = getMessageInDB(roomId);
        
        // DB에서 가져온 데이터를 큐에 저장
        messageMap.put(roomId, new LinkedList<>(chatList));
        return chatList;
    }


    public void commitMessageQueue(Queue<Chat> queue){
        for (int i = 0; i < queue.size(); i++) {
            Chat chat = queue.poll();
            em.persist(chat);
        }
        em.flush();
    }

    @Override
    public void destroy() throws Exception {
        // 서버 다운 전 큐를 db 에 저장시키기
        commitMessageQueue(messageMap.values());
    }

    private List<Chat> getMessageInDB(Long roomId) {
        return chatRepository.findNumberOfMessageInChatRoomReverse(roomId, MESSAGE_PAGEABLE_SIZE);
    }

    private List<Chat> getMessageInCache(Long roomId){
        return messageMap.get(roomId).stream().toList();
    }


    // 채팅방 삭제 시 큐 및 캐시에서 제거하는 로직 추가
    public void deleteChatRoom(Long roomId) {
        // 해당 roomId에 대한 큐를 messageMap에서 제거
        messageMap.deleteKey(roomId);
    }
}
