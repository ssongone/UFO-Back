package jungle.spaceship.notification.service;

import com.google.firebase.messaging.*;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.repository.MemberRepository;
import jungle.spaceship.notification.PushAlarm;
import jungle.spaceship.notification.controller.dto.FCMNotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public void sendMessageToFamilyExcludingMe(PushAlarm alarm, Member member) {

        Family family = member.getFamily();
        List<String> tokens =  family.getMembers().stream()
                .filter(m -> !m.equals(member))
                .map(Member::getFirebaseToken)
                .collect(Collectors.toList());

        System.out.println("tokens = " + tokens);

        List<Message> messages = tokens.stream()
                .map(token -> Message.builder()
                        .putData("time", LocalDateTime.now().toString())
                        .setNotification(alarm.toNotification())
                        .setToken(token).build())
                .collect(Collectors.toList());

        try {
            firebaseMessaging.sendEach(messages);
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }

    }


    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {

        Optional<Member> member = memberRepository.findById(requestDto.getTargetUserId());

        if (member.isPresent()) {
            if (member.get().getFirebaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        // .setImage(requestDto.getImage())
                        .build();

                Message message = Message.builder()
                        .setToken(member.get().getFirebaseToken())
                        .setNotification(notification)
                        // .putAllData(requestDto.getData())
                        .build();

                try {
                    firebaseMessaging.send(message);
                    return "알림을 성공적으로 전송했습니다. targetUserId=" + requestDto.getTargetUserId();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 보내기를 실패하였습니다. targetUserId=" + requestDto.getTargetUserId();
                }
            } else {
                return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
            }

        } else {
            return "해당 유저가 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
        }


    }

    public void sendByTokenList(List<String> tokenList) {
        Notification notification = Notification.builder()
                .setTitle("제목")
                .setBody("내용")
                // .setImage(requestDto.getImage())
                .build();
        // 메시지 만들기
        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(notification)
                .setToken(token)
                .build()).collect(Collectors.toList());

        // 요청에 대한 응답을 받을 response
        BatchResponse response;
        try {

            // 알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messages);

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokenList.get(i));
                    }
                }
                log.error("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }



}
