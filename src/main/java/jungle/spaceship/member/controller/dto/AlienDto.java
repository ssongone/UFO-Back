package jungle.spaceship.member.controller.dto;

import jungle.spaceship.member.entity.alien.AlienColor;
import jungle.spaceship.member.entity.alien.AlienType;
import lombok.Getter;

@Getter
public class AlienDto {
    private AlienType type;
    private AlienColor color;
}
