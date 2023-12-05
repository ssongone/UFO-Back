package jungle.spaceship.tmi.controller.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TmiResponseDto  {
    String content;
    String writer;

    public TmiResponseDto(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }

}
