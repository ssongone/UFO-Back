package jungle.spaceship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class MoleGameService {
    private final SimpMessageSendingOperations messagingTemplate;

    public void startGame() {
        System.out.println("MoleGameService.startGame");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int cnt = 0;
            @Override
            public void run() {
                if(cnt < 20){
                    messagingTemplate.convertAndSend("/sub/mole/room/" + 1, "두더지 보냄");
                    cnt += 1;
                }else{
                    timer.cancel();
                }
            }
        };

        TimerTask timerTask2 = new TimerTask() {
            int cnt = 0;
            @Override
            public void run() {
                if(cnt < 20){
                    messagingTemplate.convertAndSend("/sub/mole/room/" + 1, "폭탄 보냄");
                    cnt += 1;
                }else{
                    timer.cancel();
                }
            }
        };

        timer.schedule(timerTask,0,2000);
        timer.schedule(timerTask2,0,1000);
    }
}
