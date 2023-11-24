package jungle.spaceship.member.controller.dto;

import jungle.spaceship.jwt.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FamilyRegistrationDto {
    TokenInfo tokenInfo;
    String code;
    FamilyResponseDto familyResponseDto;
}
