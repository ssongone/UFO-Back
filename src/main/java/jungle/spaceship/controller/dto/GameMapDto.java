package jungle.spaceship.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class GameMapDto {
    private final double x;
    private final double y;
    private final Long familyId;
    @Setter
    private Long alienId;

}
