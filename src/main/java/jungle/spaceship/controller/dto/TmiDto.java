package jungle.spaceship.controller.dto;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Tmi;

public class TmiDto {
    String content;

    public Tmi getTmi(Member member) {
        return new Tmi(content, member);
    }
}
