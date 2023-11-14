package jungle.spaceship.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jungle.spaceship.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    // ObjectMapper : Java 와 JSON 객체를 상호 변환 처리를 도와주는 라이브러리
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    /**
     * ChatService 빈 생성 후 자동으로 호출되는
     * ChatRoom 초기화 작업
     */
    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
        /*
         * LinkedHashMap은 이중연결 리스트로 HashMap과 달리 삽입한 순서대로 출력된다.
         * HashMap 와 비교했을 때, LinkedHashMap 은 Map create 시간은 더 걸리지만, Access와 Iterate 속도가 조금 더 빠르다.
         */
    }

    /**
     * 전체 채팅방 목록 조회
     */
    public List<ChatRoom> findAllRoom(){
        log.info(String.valueOf(new ArrayList<>(chatRooms.keySet())));
        return new ArrayList<>(chatRooms.values());
    }

    /**
     * 채팅방 찾기
     */
    public ChatRoom findRoomById(String roomId){
        return chatRooms.get(roomId);
    }

    /**
     * 채팅방 생성 + UUID로 채팅방 ID 설정
     */
    public ChatRoom createRoom(String name){
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }


    public <T> void sendMessage(WebSocketSession session, T message){
        try {
            // sendMessage : 큐에 메시지를 넣고, 내부적으로 WebSocket 연결을 통해 메시지를 비동기적으로 전송
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
