package jungle.spaceship.service;


import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.oauth.KakaoInfoResponse;
import jungle.spaceship.entity.oauth.OAuthInfoResponse;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    static String OAUTH2_URL_KAKAO = "https://kapi.kakao.com/v2/user/me";
    public TokenInfo loginWithKakao(String accessToken) {
        System.out.println("MemberService.loginWithKakao");
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfo(accessToken);
        Member newMember = findOrCreateMember(oAuthInfoResponse);
        return jwtTokenProvider.generateTokenByMember(newMember.getMemberId(), newMember.getRole().getKey());
    }

    public OAuthInfoResponse requestOAuthInfo(String accessToken) {
        System.out.println("accessToken = " + accessToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        KakaoInfoResponse kakaoInfoResponse = restTemplate.postForObject(OAUTH2_URL_KAKAO, request, KakaoInfoResponse.class);
        System.out.println("kakaoInfoResponse = " + kakaoInfoResponse);

        return restTemplate.postForObject(OAUTH2_URL_KAKAO, request, KakaoInfoResponse.class);
    }

    private Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(oAuthInfoResponse)));
    }


}
