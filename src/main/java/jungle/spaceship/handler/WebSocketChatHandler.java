package jungle.spaceship.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jungle.spaceship.dto.ChatMessage;
import jungle.spaceship.dto.ChatRoom;
import jungle.spaceship.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket Handler
 * socket 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트가 접속 가능하다.
 * 이떄, 서버는 여러 클라이언트가 발송한 메시지를 받아 처리 해줄 Handler의 작성이 필요 하다.
 * WebSocketChatHandler 눈 TextWebSocketHandler 를 상속받아 Handler를 작성해 준다.
 * 클라이언트로 받은 메시지를 console log 로 출력하고 클라이언트로 환영 메시지를 보내준다.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    /**
     * 웹 소켓 Client 로부터 chatting message 를 전달 받아 chatting message 객체로 변환
     * 
     */
    private final ObjectMapper objectMapper;

    private final ChatService chatService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        /*
         * WebSocketSession
         *
         * WebSocket에는 WebSocketSession 객체가 있다.
         * 이때 WebSocketSession은 서버에 클라이언트 별 정보를 저장하는 오브젝트가 아니다.
         * WebSocket 연결의 상태, 클라이언트와 서버 간의 통신을 지원하기 위한 다양한 속성 등의 정보를 포함하는 객체이다.
         * 브라우저로부터 WebSocket 접속을 하면 서버에서 생성하며, 이 객체를 WebSocket 핸들러에서 사용하면서 필요하면 꺼내서 메시지를 보낼 수도 있다.
         * WebSocketSession은 연결이 확립될 때 한 번만 생성되며 해당 연결의 생명주기와 함께 한다.
         */


        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());

        // 채팅방 동작을 처리한다. 이때 메시지를 보낸 클라이언트의 WebSocket Session, 메시지, 서비스 객체를 인자로 전달한다.
        room.handleActions(session, chatMessage, chatService);


    }





}
