package jungle.spaceship.member.service;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.controller.dto.CalendarRequestDto;
import jungle.spaceship.member.entity.CalendarEvent;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.repository.CalendarEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarEventRepository calendarEventRepository;
    private final SecurityUtil securityUtil;
    public CalendarEvent getEvent(Long eventId) {
        return calendarEventRepository.findById(eventId).orElseThrow(()->
                new NoSuchElementException("해당 이벤트가 존재하지 않습니다"));
    }

    public CalendarEvent addEvent(CalendarRequestDto dto) {
        Member member = securityUtil.extractMember();
        CalendarEvent calendarEvent = new CalendarEvent(dto, member);
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
}
