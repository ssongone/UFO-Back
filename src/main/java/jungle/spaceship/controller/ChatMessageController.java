package jungle.spaceship.controller;

import jungle.spaceship.dto.ChatMessageDTO;
import jungle.spaceship.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    /**
     * WebSocket 으로 들어오는 메시지 발행(Publish)을 처리

     * 1. 클라이언트에서 prefix를 붙여 /pub/chat 로 발행 요청
     * 2. Controller 가 해당 메시지를 받아 처리
     * 3. 메시지 발행되면 /sub/chat/room/{roomId} 는 채팅방을 구분하는 값이므로 pub-sub 에서 Topic 의 역할
     */
    @MessageMapping("/chat")
    public void message(ChatMessageDTO message, StompHeaderAccessor accessor) {
        Authentication authentication = (Authentication) accessor.getUser();
        System.out.println("authentication = " + authentication);
        UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
        System.out.println("principal = " + principal);
        Long memberId = Long.valueOf(principal.getName());
        System.out.println("memberId = " + memberId);
        ChatMessageDTO resMessage = messageService.sendMessage(message, memberId);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), resMessage);
    }

}
