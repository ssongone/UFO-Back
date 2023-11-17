package jungle.spaceship.controller;

import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/login/kakao")
    public ResponseEntity<TokenInfo> loginFromKakao(@RequestBody String token) {
        System.out.println("MemberController.loginFromKakao");
        System.out.println("token = " + token);
        TokenInfo res = memberService.loginWithKakao(token);
        System.out.println("res = " + res);
        return ResponseEntity.ok(res);
    }


}
