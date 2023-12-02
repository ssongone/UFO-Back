package jungle.spaceship.member.controller.dto;

import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {
    TokenInfo tokenInfo;
    Member member;
    FamilyResponseDto familyResponseDto;
}
