package jungle.spaceship.photo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PhotoResponseDto {
    private Long photoId;
    private LocalDateTime createAt;

}
