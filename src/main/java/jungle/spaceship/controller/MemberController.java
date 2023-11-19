package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.AlienDto;
import jungle.spaceship.controller.dto.FamilyDto;
import jungle.spaceship.controller.dto.FamilyRegistrationDto;
import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import jungle.spaceship.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/login/kakao")
    public ResponseEntity<ExtendedResponse<TokenInfo>> loginFromKakao(@RequestBody String token) {
        ExtendedResponse<TokenInfo> result = memberService.loginWithKakao(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/register/user")
    public ResponseEntity<BasicResponse> signUp(@RequestBody SignUpDto dto) {
        memberService.signUp(dto);
        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원가입 성공!"));
    }

    @PostMapping("/api/register/alien")
    public ResponseEntity<BasicResponse> registerAlien(@RequestBody AlienDto dto) {
        memberService.registerAlien(dto);
        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "에일리언 등록 성공!"));
    }

    @GetMapping("/api/register/familyCode")
    public ResponseEntity<String> makeNewCode() {
        return ResponseEntity.ok(memberService.makeCode());
    }

    @PostMapping("/api/register/newFamily")
    public ResponseEntity<ExtendedResponse<FamilyRegistrationDto>> registerFamily(@RequestBody FamilyDto dto) {
        return ResponseEntity.ok(memberService.registerFamily(dto));
    }

    @GetMapping("/api/register/currentFamily/{code}")
    public ResponseEntity<ExtendedResponse<FamilyRegistrationDto>> registerCurrentFamily(@PathVariable String code) {
        return ResponseEntity.ok(memberService.registerCurrentFamily(code));
    }
}
