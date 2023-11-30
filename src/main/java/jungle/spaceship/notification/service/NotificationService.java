package jungle.spaceship.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.notification.PushAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessageToFamilyExcludingMe(PushAlarm alarm, Member member) {
        System.out.println("member = " + member.getNickname());
        Family family = member.getFamily();
        if (family.getMembers().size() < 2) {
            return;
        }

        List<String> tokens =  family.getMembers().stream()
                .filter(m -> !m.equals(member))
                .map(Member::getFirebaseToken)
                .collect(Collectors.toList());

        List<Message> messages = tokens.stream()
                .map(token -> {
                    Message.Builder builder = Message.builder()
                        .putData("time", LocalDateTime.now().toString())
                        .setNotification(alarm.toNotification())
                        .setToken(token);
                    Optional.ofNullable(alarm.getAdditionalData())
                            .ifPresent(additionalData -> additionalData.forEach(builder::putData));

                    return builder.build();
                })
                .collect(Collectors.toList());
        try {
            firebaseMessaging.sendEach(messages);
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

}
