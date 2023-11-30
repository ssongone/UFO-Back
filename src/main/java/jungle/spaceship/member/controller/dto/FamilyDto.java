package jungle.spaceship.member.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.notification.PushAlarm;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FamilyDto implements PushAlarm {
    private String ufoName;
    private String plantName;
    private String code;
    private String createdAt;
    private String firebaseToken;

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle("찌릿찌릿 우주선 정보가 변경되었어요")
                .setBody(DEFAULT_BODY).build();
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();

        additionalData.put("ufoName", this.ufoName);
        additionalData.put("plantName", this.plantName);

        return additionalData;
    }
}
