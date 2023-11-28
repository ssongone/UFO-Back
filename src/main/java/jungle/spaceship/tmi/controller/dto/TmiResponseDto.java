package jungle.spaceship.tmi.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.notification.PushAlarm;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class TmiResponseDto implements PushAlarm {
    String content;
    String writer;

    public TmiResponseDto(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }

    @Override
    public Notification toNotification() {

        Notification notification = Notification.builder()
                .setTitle("찌릿찌릿 " + writer + "님이 새로운 TMI를 작성했어요")
                .setBody(content)
                .build();

        return notification;
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();
        additionalData.put("content", content);
        additionalData.put("writer", writer);

        return additionalData;
    }


}
