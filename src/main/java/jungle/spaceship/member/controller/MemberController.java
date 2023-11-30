package jungle.spaceship.member.controller;

import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.member.controller.dto.*;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.service.MemberService;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
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

    @GetMapping("/api/login/kakaoRedirect")
    public void loginFromKakao22() {
        System.out.println("MemberController.loginFromKakaoRedirect");
    }

    @PostMapping("/api/register/user")
    public ResponseEntity<BasicResponse> signUp(@RequestBody SignUpDto dto) {
        memberService.signUp(dto);
        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원가입 성공!"));
    }

    @GetMapping("/api/familyInfo/{familyId}")
    public FamilyInfoResponseDto requestFamilyInfo(@PathVariable Long familyId) {
        return memberService.requestFamilyInfo(familyId);
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

    @PatchMapping("/api/member")
    public Member updateMember(@RequestBody CharacterDto dto) {
        return memberService.updateCharacter(dto);
    }

    @PatchMapping("api/family")
    public Family updateFamily(@RequestBody FamilyDto dto) {
        return memberService.updateFamily(dto);
    }

}
