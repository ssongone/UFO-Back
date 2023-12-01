package jungle.spaceship.member.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.notification.PushAlarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PlantStateDto implements PushAlarm {
    String plantName;
    int point;
    int level;
    boolean isUp;

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle("찌릿찌릿 " + plantName + " 레벨 업!")
                .setBody(NEW_FAMILY_ALARM_BODY).build();
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();

        additionalData.put("point", String.valueOf(point));
        additionalData.put("level", String.valueOf(level));

        return additionalData;
    }

}
