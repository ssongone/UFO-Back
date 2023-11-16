package jungle.spaceship.config;

import jungle.spaceship.jwt.*;
import jungle.spaceship.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2MemberSuccessHandler oAuth2MemberSuccessHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String[] URL_TO_PERMIT = {
            "/api/login",
            "/api/signup",
            "/oauth2/**",
            "/", "/css/**", "/images/**", "/js/**", "/h2-console/**"
    };
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //HTTP 기본 인증을 비활성화
                .csrf().disable()//실제 운영환경에서 지워야 할지도?  위의 Filter의 조건에 csrf설정을 비활성화 시켰는데, 이는 세션방식을 이용하지 않고, 토큰 방식을 이용하고 있기 때문에 csrf를 통한 인증이 불필요하기 때문입니다.
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                    .headers().frameOptions().disable() //  H2 데이터베이스 콘솔과 같이 내부 도구를 사용할 때 필요한 설정

                .and()
                    .exceptionHandling()                //예외처리
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                    .authorizeRequests()
                        .antMatchers(URL_TO_PERMIT).permitAll()
//                        .antMatchers("/api/ufo/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()

                .and()
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2MemberSuccessHandler)
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService));

        http
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }
}