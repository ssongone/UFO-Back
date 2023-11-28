package jungle.spaceship.member.controller;

import jungle.spaceship.member.controller.dto.CalendarRequestDto;
import jungle.spaceship.member.entity.CalendarEvent;
import jungle.spaceship.member.service.CalendarService;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendarEvent/{eventId}")
    public ExtendedResponse<CalendarEvent> getEvent(@PathVariable Long eventId) {
        CalendarEvent calendarEvent = calendarService.getEvent(eventId);
        return new ExtendedResponse<>(calendarEvent, HttpStatus.OK.value(), "");

    }

    @PostMapping("/calendarEvent")
    public ExtendedResponse<CalendarEvent> addEvent(@RequestBody CalendarRequestDto dto) {
        CalendarEvent calendarEvent = calendarService.addEvent(dto);
        return new ExtendedResponse<>(calendarEvent, HttpStatus.OK.value(), "이벤트 추가 완료");
    }

    @PatchMapping("/calendarEvent/{eventId}")
    public ExtendedResponse<CalendarEvent> updateEvent(@RequestBody CalendarRequestDto dto, @PathVariable Long eventId) {
        CalendarEvent calendarEvent = calendarService.updateEvent(dto, eventId);
        return new ExtendedResponse<>(calendarEvent, HttpStatus.OK.value(), "업데이트 완료");
    }

    @DeleteMapping("/calendarEvent/{eventId}")
    public BasicResponse deleteEvent(@PathVariable Long eventId) {
        calendarService.deleteEvent(eventId);
        return new BasicResponse(HttpStatus.OK.value(), "삭제 완료");
    }
}
