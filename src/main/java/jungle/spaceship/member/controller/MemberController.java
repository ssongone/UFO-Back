package jungle.spaceship.member.controller;

import jungle.spaceship.member.controller.dto.*;
import jungle.spaceship.member.service.MemberService;
import jungle.spaceship.response.BasicResponse;
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
                .orElseGet(() -> new ExtendedResponse<>(null, HttpStatus.BAD_REQUEST.value(), "회원가입이 필요합니다."));
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

    @PostMapping("/api/user/newFamily")
    public ResponseEntity<LoginResponseDto> signUpNewFamily(@RequestBody SignUpDto dto) {
        LoginResponseDto loginResponseDto = memberService.signUpNewFamily(dto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/api/user/currentFamily")
    public ResponseEntity<LoginResponseDto> signUpCurrentFamily(@RequestBody SignUpDto dto) {
        LoginResponseDto loginResponseDto = memberService.signUpCurrentFamily(dto);
        return ResponseEntity.ok(loginResponseDto);
    }

//    @PostMapping("/api/register/user")
//    public ResponseEntity<BasicResponse> signUp(@RequestBody SignUpDto dto) {
//        memberService.signUp(dto);
//        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원가입 성공!"));
//    }

    @GetMapping("/api/familyInfo/{familyCode}")
    public FamilyInfoResponseDto requestFamilyInfo(@PathVariable String familyCode) {
        return memberService.requestFamilyInfo(familyCode);
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

    @PostMapping("/api/register/currentFamily")
    public ResponseEntity<ExtendedResponse<FamilyRegistrationDto>> registerCurrentFamily(@RequestBody FamilyDto dto) {
        return ResponseEntity.ok(memberService.registerCurrentFamily(dto));
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
