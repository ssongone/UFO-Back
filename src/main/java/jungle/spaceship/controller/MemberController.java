package jungle.spaceship.controller;

import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.service.CustomOAuth2UserService;
import jungle.spaceship.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/login/kakao")
    public ResponseEntity<TokenInfo> loginFromKakao(@RequestBody String token) {
        TokenInfo res = memberService.loginWithKakao(token);
        System.out.println("res = " + res);
        return ResponseEntity.ok(res);
    }


//    @GetMapping("/oauth2/kakao")
//    public ResponseEntity<TokenResponseDTO> oauth2Google(@RequestParam("id_token") String idToken) throws ParseException, JsonProcessingException {
//        Map<String, Object> memberMap =  customOAuth2UserService.findOrSaveMember(idToken, "kakao");
//
//        TokenInfo tokenDTO = jwtTokenProvider.generateToken((MemberDTO) memberMap.get("dto"));
//
//        ResponseCookie responseCookie = ResponseCookie
//                .from("refresh_token", tokenDTO.getRefreshToken())
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("None")
//                .maxAge(tokenDTO.getDuration())
//                .path("/")
//                .build();
//
//        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
//                .isNewMember(false)
//                .accessToken(tokenDTO.getAccessToken())
//                .build();
//
//        return ResponseEntity.status((Integer) memberMap.get("status")).header("Set-Cookie", responseCookie.toString()).body(tokenResponseDTO);
//    }

    @PostMapping("/api/signup")
    public ResponseEntity signup(@RequestBody SignUpDto signUpDto,
                                 @AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("MemberController.signup");
        memberService.signUp(signUpDto, oAuth2User);
        BasicResponse basicResponse = new BasicResponse(HttpStatus.CREATED.value(), "회원가입 완료");
        return new ResponseEntity(basicResponse, HttpStatus.CREATED);
    }

    @GetMapping("/private/login")
    public ResponseEntity loginCallback(
//            @RequestParam(name = "loginSuccess") boolean loginSuccess,
            @RequestParam(name = "accessToken") String accessToken,
            @RequestParam(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {

//        if (loginSuccess) {
            response.addHeader("Authorization", accessToken);
            response.addHeader("refresh", refreshToken);
            BasicResponse basicResponse = new BasicResponse(HttpStatus.ACCEPTED.value(), "로그인 완료");
            return new ResponseEntity(basicResponse, HttpStatus.ACCEPTED);
//        }
    }
//    @GetMapping("/private/login")
//    public @ResponseBody ResponseEntity<ExtendedResponse<Role>> loginCallback(
//            @RequestParam(name = "loginSuccess") boolean loginSuccess,
//            @RequestParam(name = "accessToken", required = false) String accessToken,
//            @RequestParam(name = "refreshToken", required = false) String refreshToken,
//            HttpServletResponse response) {
//
//        if (loginSuccess) {
//            response.addHeader("Authorization", accessToken);
//            response.addHeader("refresh", refreshToken);
//            ExtendedResponse<Role> extendedResponse = new ExtendedResponse<>(Role.USER, HttpStatus.ACCEPTED.value(), "로그인 완료");
//            return new ResponseEntity<>(extendedResponse, HttpStatus.ACCEPTED);
//        } else {
//            ExtendedResponse<Role> extendedResponse = new ExtendedResponse<>(Role.GUEST, HttpStatus.CREATED.value(), "회원가입을 진행해야 합니당");
//            return new ResponseEntity<>(extendedResponse, HttpStatus.CREATED);
//        }
//    }


}
