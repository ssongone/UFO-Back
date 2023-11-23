package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.GameMapDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameMapController {

    private final SimpMessageSendingOperations messagingTemplate;

    // 게임방 입장할 때 보내는 데이터
    @MessageMapping("/map")
    public void gameMap(GameMapDto gameMapDto){
        messagingTemplate.convertAndSend("/sub/map/" + gameMapDto.getFamilyId(), gameMapDto);

    }
}
