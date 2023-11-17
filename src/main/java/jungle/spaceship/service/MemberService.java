package jungle.spaceship.service;


import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.oauth.KakaoInfoResponse;
import jungle.spaceship.entity.oauth.OAuthInfoResponse;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.repository.MemberRepository;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    static String OAUTH2_URL_KAKAO = "https://kapi.kakao.com/v2/user/me";
    public ExtendedResponse<TokenInfo> loginWithKakao(String accessToken) {
        System.out.println("MemberService.loginWithKakao");
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfo(accessToken);

        Optional<Member> memberByEmail = memberRepository.findByEmail(oAuthInfoResponse.getEmail());
        Member member = memberByEmail.orElseGet(() -> memberRepository.save(new Member(oAuthInfoResponse)));

        HttpStatus responseStatus = memberByEmail.isPresent() ? HttpStatus.OK : HttpStatus.CREATED;

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getMemberId(), member.getRole().getKey());
        return new ExtendedResponse<>(tokenInfo, responseStatus.value(), "로그인 완료");
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

    public void signUp(SignUpDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long memberId = Long.valueOf(user.getUsername());

        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다")
        );

        member.update(dto);

        memberRepository.save(member);
    }


}
