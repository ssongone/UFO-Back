package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.MoleCaughtInfoDto;
import jungle.spaceship.service.MoleGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/mole")
public class MoleGameController {

    private final MoleGameService moleGameService;

    @MessageMapping("/start")
    public void makeGame() {
        moleGameService.startGame();
    }


    @MessageMapping("/catch")
    public void message(MoleCaughtInfoDto caughtInfoDto) {
        System.out.println("caughtInfoDto = " + caughtInfoDto);
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), resMessage);
    }


}
