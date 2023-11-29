package jungle.spaceship.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.firebase.messaging.Notification;
import jungle.spaceship.member.controller.dto.CalendarRequestDto;
import jungle.spaceship.notification.PushAlarm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class CalendarEvent implements PushAlarm {

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


    @Override
    public Notification toNotification() {
        String newMemberName = this.member.getNickname();

        return Notification.builder()
                .setTitle("찌릿찌릿 " + newMemberName + "가 일정을 등록했어요")
                .setBody(eventName).build();
    }

    @Override
    public Map<String, String> getAdditionalData() {
        return null;
    }
}
