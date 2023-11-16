package jungle.spaceship.jwt;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Role;
import jungle.spaceship.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
@Transactional
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String authId = (String) attributes.get("email");
        Member findMember = memberRepository.findByEmail(authId).orElseThrow(() -> new NoSuchElementException("값을 찾을 수 없습니다."));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/private/login");

//        if (findMember.getRole() == Role.GUEST) {
//            System.out.println("회원가입으로 리다이렉트");
//            String redirectionUri = uriBuilder
//                    .queryParam("loginSuccess",false)
//                    .build()
//                    .toUriString();
//
//            response.sendRedirect(redirectionUri);
//            return;
//        }

        TokenInfo tokenInfo = jwtProvider.generateToken(oAuth2User);
        String accessToken = tokenInfo.getGrantType()+ " " + tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        System.out.println("tokenInfo = " + tokenInfo);

        String redirectionUri = uriBuilder
//                .queryParam("loginSuccess", true)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString();

        response.sendRedirect(redirectionUri);
    }
//
//    private void redirect(HttpServletRequest request, HttpServletResponse response, String email, List<String> authorities) throws IOException {
//        log.info("Token 생성 시작");
//        String accessToken = delegateAccessToken(email, authorities);  // Access Token 생성
//        String refreshToken = delegateRefreshToken(email);     // Refresh Token 생성
//        User user = userService.findByEmail(email);
//        Long userId = user.getUserId();
//        String username = user.getUsername();
//        user.setRefreshToken(refreshToken);
//        userService.saveUser(user);
//
//        String uri = createURI(accessToken, refreshToken, userId, username).toString();   // Access Token과 Refresh Token을 포함한 URL을 생성
//        getRedirectStrategy().sendRedirect(request, response, uri);   // sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
//    }
//
//    // Access Token 생성
//    private String delegateAccessToken(String username, List<String> authorities) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);
//        claims.put("roles", authorities);
//
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
//
//        return accessToken;
//    }
//
//    // Refresh Token 생성
//    private String delegateRefreshToken(String username) {
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
//
//        return refreshToken;
//    }
//
//    // Redirect URI 생성. JWT를 쿼리 파라미터로 담아 전달한다.
//    private URI createURI(String accessToken, String refreshToken, Long userId, String username) {
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("user_id", String.valueOf(userId));
//        queryParams.add("username", username);
//        queryParams.add("access_token", accessToken);
//        queryParams.add("refresh_token", refreshToken);
//
//        return UriComponentsBuilder
//                .newInstance()
//                .scheme("https")
//                .host("66challenge.shop")
//                .path("/oauth")
//                .queryParams(queryParams)
//                .build()
//                .toUri();
//    }
}
