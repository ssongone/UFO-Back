package jungle.spaceship.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlantStateDto {
    String plantName;
    int point;
    boolean isUp;
}
