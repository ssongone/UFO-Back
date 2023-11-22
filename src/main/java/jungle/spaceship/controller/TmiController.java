package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.TmiDto;
import jungle.spaceship.entity.Attendance;
import jungle.spaceship.entity.Tmi;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.service.TmiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TmiController {

    private final TmiService tmiService;

    @PostMapping("/tmi")
    public ResponseEntity<BasicResponse> registerTmi(@RequestBody TmiDto tmiDto){
        return ResponseEntity.ok(tmiService.registerTmi(tmiDto));
    }

    @GetMapping("/tmi/{familyId}")
    public ResponseEntity<ExtendedResponse<List<Tmi>>> getTmiByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(tmiService.getTmiByFamilyId(familyId));
    }

    @GetMapping("/tmi/check")
    public ResponseEntity<BasicResponse> checkTmiBeforeAttendance(){
        return ResponseEntity.ok(tmiService.tmiCheck());
    }

    @GetMapping("/attendance")
    public ResponseEntity<BasicResponse> attend() {
        return ResponseEntity.ok(tmiService.attend());
    }

    @GetMapping("/attendance/{familyId}")
    public ResponseEntity<ExtendedResponse<List<Attendance>>> getAttendees(@PathVariable Long familyId) {
        return ResponseEntity.ok(tmiService.getAttendees(familyId));
    }




}
