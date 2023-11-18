package jungle.spaceship.config;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() { // 블로킹 방식 (요청 보내고 기다리는 동안 스레드 차단됨) <-> WebClient 논 블로킹 방식
        return new RestTemplate();
    }

    @Bean
    public SecurityUtil securityUtil(MemberRepository memberRepository) {
        return new SecurityUtil(memberRepository);
    }

}
