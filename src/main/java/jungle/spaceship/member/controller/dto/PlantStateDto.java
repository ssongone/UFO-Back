package jungle.spaceship.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PlantStateDto {
    String plantName;
    int point;
    int level;
    boolean isUp;
}
