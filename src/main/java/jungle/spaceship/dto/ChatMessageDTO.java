package jungle.spaceship.dto;

import jungle.spaceship.entity.ChatRoom;
import jungle.spaceship.entity.Message;
import jungle.spaceship.entity.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 채팅 메시지에 대한 정보 담기
 *
 */
@Getter
@RequiredArgsConstructor
public class ChatMessageDTO {

    private final MessageType type;
    private final Long roomId;
    private final String sender;    // 회원 닉네임
    private final Long memberId;

    @Setter
    private String content;
    private final String time;

    public Message getNewMessage(ChatRoom room, Long memberId){
        return Message.builder()
                .messageType(type)
                .content(content)
                .memberId(memberId)
                .chatRoom(room)
                .createAt(time)
                .build();

    }
}
