package jungle.spaceship.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TmiDto {
    String content;

    public TmiDto(String content) {
        this.content = content;
    }

}
