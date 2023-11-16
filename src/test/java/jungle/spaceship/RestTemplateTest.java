package jungle.spaceship;


import jungle.spaceship.entity.auth.KakaoInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
public class RestTemplateTest {
    private final RestTemplate restTemplate = new RestTemplate();
    String URL = "https://kapi.kakao.com/v2/user/me";
    String accessToken = "Khe-aWY9ZBGho-bJ2z8ZMQidlHZ7A_spCf8KPXWbAAABi9hluv3HP8VuE1ZNOQ";


    @Test
    void hi() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoInfoResponse kakaoInfoResponse = restTemplate.postForObject(URL, request, KakaoInfoResponse.class);
        System.out.println("kakaoInfoResponse = " + kakaoInfoResponse);
    }


//    @Test
//    void 카카오_로그인_테스트() {
//        TokenInfo res = memberService.loginWithKakao(accessToken);
//        System.out.println("res = " + res);
//    }


}
