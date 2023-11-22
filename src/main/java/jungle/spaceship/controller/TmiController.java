package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.TmiDto;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.service.TmiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TmiController {

    private final TmiService tmiService;

    @PostMapping("/tmi")
    public ResponseEntity<ExtendedResponse<TmiDto>> registerTmi(@RequestBody TmiDto tmiDto){
        return ResponseEntity.ok(tmiService.registerTmi(tmiDto));

    }

}
