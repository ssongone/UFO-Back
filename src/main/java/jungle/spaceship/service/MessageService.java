package jungle.spaceship.service;

import jungle.spaceship.dto.ChatMessageDTO;
import jungle.spaceship.entity.*;
import jungle.spaceship.repository.MessageRepository;
import jungle.spaceship.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService implements DisposableBean{

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅 메시지 임시 저장 캐시 : 채팅방Id, 채팅 메시지
    private static final Map<Long, Queue<Message>> messageMap = new HashMap<>();
    private final EntityManager em;


    private static final int TRANSACTION_MESSAGE_SIZE = 20; // 한번에 처리될 메시지 사이즈
    private static final int MESSAGE_PAGEABLE_SIZE = 30;    // Queue 에 임시 보관될 메시지 수
    private static final int MESSAGE_CACHE_MAX = 10;        // Write Back 패턴 중 최대 모을 수 있는 메시지 캐시


    /**
     * 메시지 보내기 및 캐시/DB 에 저장
     */
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        if(MessageType.ENTER.equals(message.getType())) {

            message.setContent(message.getSender() + "(님)이 입장하였습니다.");
        }
        saveMessage(message.getRoomId(), message.getType(), message.getContent(), message.getMemberId());
        return message;
    }


    private void saveMessage(Long roomId, MessageType messageType, String content, Long memberId){
        ChatRoom chatRoom =
                chatRoomRepository.findById(roomId).orElseThrow();

//        Member member =
//                memberRepository.findById(memberId).orElseThrow();
        // test 용 주입
        Member member = new Member();

        Message message =
                new Message(messageType, content, member, chatRoom);

        // 채팅방에 캐시가 없다면 새로운 큐를 생성 및 메시지 추가 후 put(roomId, queue) 한다.
        messageMap.computeIfAbsent(roomId, key -> new LinkedList<>()).add(message);
        Queue<Message> queue = messageMap.get(roomId);

        // 캐시 쓰기 전략 (Write Back) : 큐 사이즈가 일정 크기 초과하면 일부를 db에 저장 후 큐를 갱신
        if(queue.size() > TRANSACTION_MESSAGE_SIZE + MESSAGE_PAGEABLE_SIZE){

            Queue<Message> tmpQueue = new LinkedList<>();
            for (int i = 0; i < TRANSACTION_MESSAGE_SIZE; i++) {
                
                tmpQueue.add(queue.poll()); 
            }
            commitMessageQueue(tmpQueue);   // 큐에서 메시지를 가져와 db에 저장
        }

        // 큐의 상태가 변경되었기 때무넹 현재 큐를 갱신
        messageMap.put(roomId, queue);
    }

    /**
     * 채팅 메시지 페이징 처리
     */
    public List<Message> getMessages(Long roomId) {
        // 캐시 읽기 전략 (LookAside) : 1차로 큐에 메시지가 저장되어있는지 확인 후, 없다면 db에서 조회

        // 큐에서 조회
        if(messageMap.containsKey(roomId)){
            return getMessageInCache(roomId);
        }
        // DB에서 조회
        List<Message> messageList = getMessageInDB(roomId);
        
        // DB에서 가져온 데이터를 큐에 저장
        messageMap.put(roomId, new LinkedList<>(messageList));
        return messageList;
    }


    private void commitMessageQueue(Queue<Message> queue){
        for (int i = 0; i < queue.size(); i++) {
            Message message = queue.poll();
            em.persist(message);
        }
        em.flush();
    }

    @Override
    public void destroy() throws Exception {
        // 서버 다운 전 큐를 db 에 저장시키기
        for (Queue<Message> messages : messageMap.values()) {
            commitMessageQueue(messages);
        }
    }

    private List<Message> getMessageInDB(Long roomId) {
        return messageRepository.findNumberOfMessageInChatRoomReverse(roomId,MESSAGE_PAGEABLE_SIZE);
    }

    private List<Message> getMessageInCache(Long roomId){
        return messageMap.get(roomId).stream().toList();
    }
}
