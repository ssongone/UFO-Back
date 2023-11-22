package jungle.spaceship.controller.dto;

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

    @Setter
    private String content;
    private final String time;

    public Message getNewMessage(Long memberId){
        return Message.builder()
                .messageType(type)
                .content(content)
                .memberId(memberId)
                .roomId(roomId)
                .createAt(time)
                .build();

    }
}
