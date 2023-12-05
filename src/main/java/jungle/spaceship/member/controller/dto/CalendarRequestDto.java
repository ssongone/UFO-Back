package jungle.spaceship.member.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CalendarRequestDto {
    private String eventName;
    private String memo;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
