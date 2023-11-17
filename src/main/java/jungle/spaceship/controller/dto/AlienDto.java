package jungle.spaceship.controller.dto;

import jungle.spaceship.entity.AlienColor;
import jungle.spaceship.entity.ElienType;
import lombok.Getter;

@Getter
public class AlienDto {
    private ElienType type;
    private AlienColor color;
}
