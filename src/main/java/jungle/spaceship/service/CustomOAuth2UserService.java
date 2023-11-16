package jungle.spaceship.service;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.auth.OAuthAttributes;
import jungle.spaceship.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
//    private final HttpSession httpSession;
    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // Resource Server(구글, 카카오, 네이버)마다 보내주는 데이터가 다르기 때문에 OAuth2Attribute에 전달하여 처리해준다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
//
        Member member = memberRepository.findByEmail(attributes.getEmail()).orElse(attributes.toEntity());
        memberRepository.save(member);

//        httpSession.setAttribute("user", new SessionMember(member));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(attributes.toEntity().getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

//    private Member saveOrUpdate(OAuthAttributes attributes){
//        Member member = memberRepository.findByEmail(attributes.getEmail())
//                        .orElse(attributes.toEntity());
//
//        return memberRepository.save(member);
//    }

}
