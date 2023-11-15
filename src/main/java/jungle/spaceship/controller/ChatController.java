package jungle.spaceship.controller;

import jungle.spaceship.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * WebSocket 으로 들어오는 메시지 발행(Publish)을 처리

     * 1. 클라이언트에서 prefix를 붙여 /pub/chat/message 로 발행 요청
     * 2. Controller 가 해당 메시지를 받아 처리
     * 3. 메시지 발행되면 /sub/chat/room/{roomId} 는 채팅방을 구분하는 값이므로 pub-sub 에서 Topic 의 역할
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message){
        if(ChatMessage.MessageType.ENTER.equals(message.getType()))
            message.setMessage(message.getSender() + "(님)이 입장하였습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
