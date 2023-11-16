package jungle.spaceship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate() { // 블로킹 방식 (요청 보내고 기다리는 동안 스레드 차단됨) <-> WebClient 논 블로킹 방식
        return new RestTemplate();
    }
}
