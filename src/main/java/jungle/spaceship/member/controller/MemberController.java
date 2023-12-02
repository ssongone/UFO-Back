package jungle.spaceship.member.controller;

import jungle.spaceship.member.controller.dto.FamilyInfoResponseDto;
import jungle.spaceship.member.controller.dto.LoginResponseDto;
import jungle.spaceship.member.controller.dto.SignUpDto;
import jungle.spaceship.member.service.MemberService;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/login/kakao")
    public ExtendedResponse<LoginResponseDto> loginFromKakao(@RequestBody String token) {
        Optional<LoginResponseDto> loginResponseDto = memberService.loginWithKakao(token);
        return loginResponseDto.map(responseDto -> new ExtendedResponse<>(responseDto, HttpStatus.OK.value(), "로그인 성공"))
                .orElseGet(() -> new ExtendedResponse<>(null, 12345, "회원가입이 필요합니다."));
    }

    @GetMapping("/api/login/token")
    public ExtendedResponse<LoginResponseDto> loginFromToken() {
        LoginResponseDto loginResponseDto = memberService.loginWithToken();
        return new ExtendedResponse<>(loginResponseDto, HttpStatus.OK.value(), "로그인 성공");
    }

    @GetMapping("/api/login/kakaoRedirect")
    public void loginFromKakao22() {
        System.out.println("MemberController.loginFromKakaoRedirect");
    }

    @PostMapping("/api/register/newFamily")
    public ExtendedResponse<LoginResponseDto> signUpNewFamily(@RequestBody SignUpDto dto) {
        Optional<LoginResponseDto> loginResponseDto = memberService.signUpNewFamily(dto);
        return loginResponseDto.map(responseDto -> new ExtendedResponse<>(responseDto, HttpStatus.OK.value(), "회원가입 성공"))
                .orElseGet(() -> new ExtendedResponse<>(null, 12345, "이메일을 찾을 수 없습니다"));

    }

    @PostMapping("/api/register/currentFamily")
    public ExtendedResponse<LoginResponseDto> signUpCurrentFamily(@RequestBody SignUpDto dto) {
        Optional<LoginResponseDto> loginResponseDto = memberService.signUpCurrentFamily(dto);
        return loginResponseDto.map(responseDto -> new ExtendedResponse<>(responseDto, HttpStatus.OK.value(), "회원가입 성공"))
                .orElseGet(() -> new ExtendedResponse<>(null, 12345, "이메일을 찾을 수 없습니다"));
    }


    @GetMapping("/api/familyInfo/{familyCode}")
    public FamilyInfoResponseDto requestFamilyInfo(@PathVariable String familyCode) {
        return memberService.requestFamilyInfo(familyCode);
    }

    @GetMapping("/api/register/familyCode")
    public ResponseEntity<String> makeNewCode() {
        return ResponseEntity.ok(memberService.makeCode());
    }

    @GetMapping("/api/register/familyCode/{code}")
    public ResponseEntity<Boolean> makeNewCode(@PathVariable String code) {
        return ResponseEntity.ok(memberService.validateCode(code));
    }


//    @PatchMapping("/api/member")
//    public Member updateMember(@RequestBody CharacterDto dto) {
//        return memberService.updateCharacter(dto);
//    }
//
//    @PatchMapping("api/family")
//    public Family updateFamily(@RequestBody FamilyDto dto) {
//        return memberService.updateFamily(dto);
//    }

}
