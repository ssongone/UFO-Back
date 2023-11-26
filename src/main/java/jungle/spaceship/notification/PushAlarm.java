package jungle.spaceship.notification;

import com.google.firebase.messaging.Notification;

public interface PushAlarm {

    String NEW_FAMILY_ALARM_BODY = "우주선에 새로운 에일리언이 들어왔어요";

    Notification toNotification();
}
