package jungle.spaceship.service;


import jungle.spaceship.controller.dto.AlienDto;
import jungle.spaceship.controller.dto.FamilyDto;
import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.entity.Alien;
import jungle.spaceship.entity.Family;
import jungle.spaceship.entity.InvitationCode;
import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.oauth.KakaoInfoResponse;
import jungle.spaceship.entity.oauth.OAuthInfoResponse;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.repository.AlienRepository;
import jungle.spaceship.repository.FamilyRepository;
import jungle.spaceship.repository.InvitationCodeRepository;
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

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Optional;

import static jungle.spaceship.entity.InvitationCode.CODE_CHARACTERS;
import static jungle.spaceship.entity.InvitationCode.CODE_LENGTH;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AlienRepository alienRepository;
    private final FamilyRepository familyRepository;
    private final InvitationCodeRepository invitationCodeRepository;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUtil securityUtil;

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
        System.out.println("member = " + member);
        memberRepository.save(member);
    }


    public void registerAlien(AlienDto dto) {
        Member member = securityUtil.extractMember();
        Alien alien = new Alien(dto);
        member.setAlien(alienRepository.save(alien));
        memberRepository.save(member);
    }
    @Transactional
    public void registerFamily(FamilyDto dto) {
        Member member = securityUtil.extractMember();
        Family family = new Family(dto);

        family.getMembers().add(member);
        member.setFamily(family);

        memberRepository.save(member);
        familyRepository.save(family);

        String code = makeCode();
        System.out.println("code = " + code);

        InvitationCode invitationCode = new InvitationCode(code, family);
        invitationCodeRepository.save(invitationCode);
    }

    public String makeCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CODE_CHARACTERS.length());
            char randomChar = CODE_CHARACTERS.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }

}
