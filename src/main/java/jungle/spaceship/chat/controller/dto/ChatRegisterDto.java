package jungle.spaceship.chat.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.chat.entity.Chat;
import jungle.spaceship.chat.entity.ChatType;
import jungle.spaceship.notification.PushAlarm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 채팅 메시지에 대한 정보 담기
 *
 */
@Getter
@RequiredArgsConstructor
public class ChatRegisterDto implements PushAlarm {

    private final ChatType type;
    private final Long roomId;
    private final String sender;    // 회원 닉네임

    @Setter
    private String content;
    private final String time;

    public Chat getNewMessage(){
        return Chat.builder()
                .chatType(type)
                .content(content)
                .sender(sender)
                .roomId(roomId)
                .createAt(time)
                .build();

    }

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle(sender)
                .setBody(content)
                .build();
    }
}
