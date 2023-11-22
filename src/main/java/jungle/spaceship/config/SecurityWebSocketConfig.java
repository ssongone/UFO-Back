package jungle.spaceship.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SecurityWebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
        message
                .nullDestMatcher().permitAll()
                .simpTypeMatchers(SimpMessageType.CONNECT,
                        SimpMessageType.DISCONNECT, SimpMessageType.OTHER).permitAll()
                .anyMessage().authenticated();
//                .simpDestMatchers("/pub/**").authenticated()
//                .simpSubscribeDestMatchers("/sub/**").authenticated()
//
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
