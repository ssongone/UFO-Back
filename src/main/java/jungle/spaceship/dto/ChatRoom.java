package jungle.spaceship.dto;

import jungle.spaceship.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {

    // final ??
    private String roomId;

    private String name;

    // 채팅방에 입장한 회원들의 정보를 갖고 있어야 한다.
    private Set<WebSocketSession> sessions = new HashSet<>();


    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }


    /**
     * 채팅방에서의 동작 처리
     */
    public void handleActions(WebSocketSession session, ChatMessage message, ChatService service){
        if(message.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            message.setMessage(message.getSender() + " (님)이 입장했습니다.");
        }
        sendMessage(message, service);
    }

    /**
     * 여러 WebSocket Session 에 주어진 메시지들을 병렬로 전송
     */
    public <T> void sendMessage(T message, ChatService service){
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));

    }

}
