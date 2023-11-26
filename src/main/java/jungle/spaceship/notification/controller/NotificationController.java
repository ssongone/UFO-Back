package jungle.spaceship.notification.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final FirebaseMessaging firebaseMessaging;


    @GetMapping("/api/notification/push")
    public String pushpush() {
        System.out.println("MemberController.pushpush");
        sendNotificationByToken();
        return "^-^";
    }

    public void sendNotificationByToken() {
        System.out.println("MemberController.sendNotificationByToken");
        Notification notification = Notification.builder()
                .setTitle("ㅈㅇㅈㅇ")
                .setBody("산책언제가")
                .build();

        Message message = Message.builder()
                .setToken("fhDmAwyQQICh4ERpe8W17Q:APA91bEx--DfIiHQ9x_neJlWi2wftTYNVPACUUkzn_dDQkI4wiQGLoQ1F9U5ai17fKWGkTt9BOJQiEgC8D4XDqGe5MpaSwjHe2BEg0RKpN8FlLVcweISv8bJ0MuwAZ6it-KjYIrIBDOi")
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
