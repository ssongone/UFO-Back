package jungle.spaceship.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jungle.spaceship.member.controller.dto.CalendarRequestDto;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    private String eventName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public CalendarEvent(CalendarRequestDto dto, Member member) {
        this.member = member;
        this.eventName = dto.getEventName();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    public CalendarEvent updateMember(Member member) {
        this.member = member;
        return this;
    }

    public CalendarEvent update(CalendarRequestDto dto) {
        this.eventName = dto.getEventName();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        return this;
    }




}
