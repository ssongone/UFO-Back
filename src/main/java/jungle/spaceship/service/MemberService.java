package jungle.spaceship.service;


import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.MemberProfile;
import jungle.spaceship.entity.Role;
import jungle.spaceship.entity.auth.KakaoInfoResponse;
import jungle.spaceship.entity.auth.OAuthInfoResponse;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.repository.MemberProfileRepository;
import jungle.spaceship.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    static String OAUTH2_URL_KAKAO = "https://kapi.kakao.com/v2/user/me";
    public TokenInfo loginWithKakao(String accessToken) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfo(accessToken);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return jwtTokenProvider.generateTokenByMemberId(memberId);

    }

    public OAuthInfoResponse requestOAuthInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(OAUTH2_URL_KAKAO, request, KakaoInfoResponse.class);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getMemberId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = new Member(oAuthInfoResponse);
        return memberRepository.save(member).getMemberId();
    }


    public void signUp(SignUpDto signUpDto, OAuth2User oAuth2User) {
        System.out.println("oAuth2User = " + oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String authId = (String) attributes.get("id");
        System.out.println("authId = " + authId);

        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(()-> new IllegalArgumentException("등록되지 않은 사용자입니다."));

        MemberProfile memberProfile = new MemberProfile(signUpDto);
        member.setMemberProfile(memberProfile);
        member.setRole(Role.USER);

        memberRepository.save(member);
        memberProfileRepository.save(memberProfile);
    }


}
