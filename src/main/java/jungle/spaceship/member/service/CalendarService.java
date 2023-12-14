package jungle.spaceship.member.service;

import jungle.spaceship.chat.service.ChatService;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.controller.dto.CalendarRequestDto;
import jungle.spaceship.member.entity.CalendarEvent;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.repository.CalendarEventRepository;
import jungle.spaceship.member.repository.FamilyRepository;
import jungle.spaceship.notification.FcmService;
import jungle.spaceship.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static jungle.spaceship.member.entity.Plant.ADD_EVENT_POINT;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarEventRepository calendarEventRepository;
    private final FcmService fcmService;
    private final FamilyRepository familyRepository;
    private final SecurityUtil securityUtil;
    private final PlantService plantService;
    private final ChatService chatService;
    public CalendarEvent getEvent(Long eventId) {
        return calendarEventRepository.findById(eventId).orElseThrow(()->
                new NoSuchElementException("해당 이벤트가 존재하지 않습니다"));
    }

    public CalendarEvent addEvent(CalendarRequestDto dto) {
        Member member = securityUtil.extractMember();
        CalendarEvent calendarEvent = new CalendarEvent(dto, member);
        fcmService.sendFcmMessageToFamilyExcludingMe(member, NotificationType.CALENDAR, dto.getEventName());
        if (member.getFamily() != null && member.getFamily().getPlant() != null) {
            familyRepository.save(member.getFamily());
            plantService.performActivity(member, ADD_EVENT_POINT);
        }
        chatService.sendCalendarEventMessage(calendarEvent, member);
        return calendarEventRepository.save(calendarEvent);
    }

    public CalendarEvent updateEvent(CalendarRequestDto dto, Long eventId) {
        CalendarEvent calendarEvent = calendarEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchElementException("해당 이벤트가 존재하지 않습니다"));
        calendarEvent.update(dto);
        return calendarEventRepository.save(calendarEvent);
    }

    public void deleteEvent(Long eventId) {
        CalendarEvent calendarEvent = calendarEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchElementException("해당 이벤트가 존재하지 않습니다"));
        calendarEventRepository.delete(calendarEvent);
    }


    public List<CalendarEvent> monthlyEvent(int year, int month) {
        Long familyId = securityUtil.extractFamilyId();
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0).minusSeconds(1);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        return calendarEventRepository.findByMember_Family_FamilyIdAndEndDateIsAfterAndStartDateIsBefore(familyId, startOfMonth, endOfMonth);
    }
}
