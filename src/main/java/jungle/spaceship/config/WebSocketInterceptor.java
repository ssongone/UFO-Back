package jungle.spaceship.config;

import jungle.spaceship.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            String authToken = accessor.getFirstNativeHeader("Authorization");

            if(StringUtils.hasText(authToken) && authToken.startsWith("Bearer"))
                authToken = authToken.substring(7);

            if (authToken == null || !jwtTokenProvider.validateToken(authToken)) {
                throw new RuntimeException("유효하지 않은 토큰입니다");
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(authToken);
            accessor.setUser(authentication);
        }
        return message;
    }



}
