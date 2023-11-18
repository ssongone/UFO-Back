package jungle.spaceship.controller.dto;

import jungle.spaceship.entity.AlienColor;
import jungle.spaceship.entity.AlienType;
import lombok.Getter;

@Getter
public class AlienDto {
    private AlienType type;
    private AlienColor color;
}
