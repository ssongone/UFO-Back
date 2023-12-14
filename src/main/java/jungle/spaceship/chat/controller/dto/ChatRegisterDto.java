package jungle.spaceship.chat.controller.dto;

import jungle.spaceship.chat.entity.Chat;
import jungle.spaceship.chat.entity.ChatType;
import lombok.*;

/**
 * 채팅 메시지에 대한 정보 담기
 *
 */
@Getter
@AllArgsConstructor
@ToString
public class ChatRegisterDto {

    private ChatType type;
    private Long roomId;
    private String sender;    // 회원 닉네임

    @Setter
    private String content;
    private String time;

    public ChatRegisterDto() {
    }

    public Chat getNewMessage(){
        return Chat.builder()
                .chatType(type)
                .content(content)
                .sender(sender)
                .roomId(roomId)
                .createAt(time)
                .build();

    }

}
