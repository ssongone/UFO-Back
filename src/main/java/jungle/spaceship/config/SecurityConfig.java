package jungle.spaceship.config;

import jungle.spaceship.entity.Role;
import jungle.spaceship.jwt.OAuth2MemberSuccessHandler;
import jungle.spaceship.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2MemberSuccessHandler oAuth2MemberSuccessHandler;


    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //HTTP 기본 인증을 비활성화
                .csrf().disable()//실제 운영환경에서 지워야 할지도?
                .headers().frameOptions().disable() //  H2 데이터베이스 콘솔과 같이 내부 도구를 사용할 때 필요한 설정
                .and()
                    .authorizeRequests()
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .antMatchers("/api/ufo/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2MemberSuccessHandler)
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService));
//                    .oauth2Login()
//                        .userInfoEndpoint()
//                            .userService(customOAuth2UserService);
        return http.build();
    }
}