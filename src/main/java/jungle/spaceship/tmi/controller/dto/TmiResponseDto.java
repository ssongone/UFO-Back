package jungle.spaceship.tmi.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.notification.PushAlarm;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TmiResponseDto implements PushAlarm {
    String content;
    String writer;

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle("찌릿찌릿 " + writer + "님이 새로운 TMI를 작성했어요")
                .setBody(this.content)
                .build();
    }
}
