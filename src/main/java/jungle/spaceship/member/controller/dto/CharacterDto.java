package jungle.spaceship.member.controller.dto;

import jungle.spaceship.member.entity.alien.AlienType;
import jungle.spaceship.member.entity.family.FamilyRole;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CharacterDto {
    String nickname;
    LocalDate birthdate;
    AlienType alienType;
    FamilyRole familyRole;
}
