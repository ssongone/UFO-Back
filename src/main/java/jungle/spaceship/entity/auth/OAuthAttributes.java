package jungle.spaceship.entity.auth;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    private String oAuthid;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String oAuthid){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.oAuthid = oAuthid;

    }

    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes){
        switch (provider) {
            case "google":
//                return ofGoogle(userNameAttributeName, attributes);
                return ofGoogle(userNameAttributeName, attributes);
            case "kakao" :
                return ofKakao(userNameAttributeName, attributes);
            default:
                throw new RuntimeException("소셜 로그인 접근 실패");
        }

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .oAuthid((String) attributes.get("sub")) // "sub"은 Google에서 사용자의 고유 ID를 나타냅니다.
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .oAuthid((String) attributes.get("sub")) // "sub"은 Google에서 사용자의 고유 ID를 나타냅니다.
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .authId(oAuthid)
                .build();
    }
}

