package jungle.spaceship.controller.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MoleCaughtInfoDto {
    private Long gameId;
    private Long moleId;
    private LocalDateTime catchTime;
}
