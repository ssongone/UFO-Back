package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/login/kakao")
    public ResponseEntity<ExtendedResponse<TokenInfo>> loginFromKakao(@RequestBody String token) {
        System.out.println("MemberController.loginFromKakao");
        System.out.println("token = " + token);
        ExtendedResponse<TokenInfo> result = memberService.loginWithKakao(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/register")
    public BasicResponse signUp(@RequestBody SignUpDto dto) {
        memberService.signUp(dto);
        return new BasicResponse(HttpStatus.OK.value(), "회원가입 성공!");
    }

}
