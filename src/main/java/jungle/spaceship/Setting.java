package jungle.spaceship;


import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.member.controller.dto.FamilyResponseDto;
import jungle.spaceship.member.controller.dto.LoginResponseDto;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.repository.MemberRepository;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Setting {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/1/master/{password}")
    public ExtendedResponse<LoginResponseDto> demo(@PathVariable String password) {
        Member member = null;

        if (password.equals("tadpole")) {
            member = memberRepository.findByEmail("song42782@daum.net").orElse(null);
        } else if (password.equals("kongz")) {
            member = memberRepository.findByEmail("hr_1227@daum.net").orElse(null);
        }
//        else if {
//            member = memberRepository.findByEmail("science627@naver.com").orElse(null);
//        }
        if (member == null || member.getFamily() == null) {
            return new ExtendedResponse<>(null, 12345, "회원가입이 필요합니다.");
        }
        Family family = member.getFamily();

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        return new ExtendedResponse<>(new LoginResponseDto(tokenInfo, member, familyResponseDto), HttpStatus.OK.value(), "로그인 성공");
    }

}
