package jungle.spaceship.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 *  WebSocketConfig
 *  Web Socket 을 사용 하는 기능에 있어 필요한 설정을 담당
 *  @EnableWebSocket 을 작성해 활성화
 *  endpoint : /ws/chat
 *  CORS :setAllowedOrigins("*")
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        // enableSimpleBroker : 스프링에서 제공하는 내장 브로커를 사용하겠다는 설정
        // /sub : Prefix 가 붙은 메시지가 송신되었을 때, 그 메시지를 브로커가 처리하겠다는 의미
        config.enableSimpleBroker("/sub");

        // setApplicationDestinationPrefixes : 바로 브로커가 아닌, 메시지에 어떤 처리/가공이 필요한 경우 해당 경로를 처리하고 있는 핸들러로 전달
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
//                .withSockJS();
    }
}
