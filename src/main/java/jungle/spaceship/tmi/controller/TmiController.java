package jungle.spaceship.tmi.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.tmi.controller.dto.TmiDto;
import jungle.spaceship.tmi.controller.dto.TmiResponseDto;
import jungle.spaceship.tmi.entity.Attendance;
import jungle.spaceship.tmi.entity.Tmi;
import jungle.spaceship.tmi.service.TmiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TmiController {

    private final TmiService tmiService;
    private final SecurityUtil securityUtil;
    private final FirebaseMessaging firebaseMessaging;

    @PostMapping("/tmi")
    public ResponseEntity<BasicResponse> registerTmi(@RequestBody TmiDto tmiDto){
        return ResponseEntity.ok(tmiService.registerTmi(tmiDto));
    }

    @GetMapping("/familyTmi")
    public List<TmiResponseDto> getTmiByFamilyId() {
        List<TmiResponseDto> tmiByFamilyId = tmiService.getTmiByFamilyId();
        System.out.println("tmiByFamilyId = " + tmiByFamilyId);
        return tmiByFamilyId;
    }

    @GetMapping("/tmi/check")
    public ResponseEntity<BasicResponse> checkTmiBeforeAttendance(){
        return ResponseEntity.ok(tmiService.tmiCheck());
    }

    @GetMapping("/attendance")
    public ResponseEntity<ExtendedResponse<PlantStateDto>> attend() {
        return ResponseEntity.ok(tmiService.attend());
    }

    @GetMapping("/familyAttendance")
    public ResponseEntity<ExtendedResponse<List<Attendance>>> getAttendees() {
        return ResponseEntity.ok(tmiService.getAttendees());
    }

    @GetMapping("/weeklyTmi")
    public ExtendedResponse<Map<Date, List<Tmi>>> weeklyTmi() {
        return tmiService.weeklyTmi();
    }

    @GetMapping("/weeklyAttendance")
    public ExtendedResponse<Map<Date, List<Attendance>>> weeklyAttendance() {
        return tmiService.weeklyAttendance();
    }

    @PostMapping("/mind627")
    public String mind627() {
        System.out.println("TmiController.mind627");
        String token = securityUtil.extractMember().getFirebaseToken();

        Notification notification = Notification.builder()
                .setTitle("푸ㅅ1알➱림ㅌㅔ亼트✒")
                .setBody("ε종윤ய은도H௩ヌ1❣")
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .putData("time", LocalDateTime.now().toString())
                .putData("songwon", "배고픔")
                .setToken(token).build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }

        return "완";
    }

}
