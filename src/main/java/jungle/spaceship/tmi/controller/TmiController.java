package jungle.spaceship.tmi.controller;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.notification.service.NotificationServiceImpl;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.tmi.controller.dto.TmiDto;
import jungle.spaceship.tmi.controller.dto.TmiResponseDto;
import jungle.spaceship.tmi.entity.Attendance;
import jungle.spaceship.tmi.service.TmiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TmiController {

    private final TmiService tmiService;
    private final SecurityUtil securityUtil;
    private final NotificationServiceImpl notificationServiceImpl;


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
    public ResponseEntity<BasicResponse> attend() {
        return ResponseEntity.ok(tmiService.attend());
    }

    @GetMapping("/familyAttendance")
    public ResponseEntity<ExtendedResponse<List<Attendance>>> getAttendees() {
        return ResponseEntity.ok(tmiService.getAttendees());
    }




}
