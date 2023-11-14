package jungle.spaceship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 채팅 메시지에 대한 정보 담기
 *
 */
@Getter
@Setter
@Builder
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK,
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String time;

}
