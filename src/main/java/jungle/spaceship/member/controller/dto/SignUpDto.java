package jungle.spaceship.member.controller.dto;

import jungle.spaceship.member.entity.alien.AlienType;
import jungle.spaceship.member.entity.family.FamilyRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {

    private String nickname;
    private LocalDate birthdate;
    private FamilyRole familyRole;
    private String firebaseToken;
    private AlienType alienType;
    private String code;

    private String ufoName;
    private String plantName;
}
