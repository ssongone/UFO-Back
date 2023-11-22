package jungle.spaceship.service;

import jungle.spaceship.controller.dto.ChatResponseDto;
import jungle.spaceship.controller.dto.TmiDto;
import jungle.spaceship.entity.Attendance;
import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Tmi;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.repository.AttendanceRepository;
import jungle.spaceship.repository.TmiRepository;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TmiService {

    private final int MAX_TMI_COUNT_PER_DAY = 3;
    private final SecurityUtil securityUtil;
    private final TmiRepository tmiRepository;
    private final AttendanceRepository attendanceRepository;


    public BasicResponse tmiCheck() {
        Member member = securityUtil.extractMember();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Long count = tmiRepository.countByMemberAndCreateAtIsAfterAndCreateAtIsBefore(member, startOfDay, endOfDay);
        if (count < 1) {
            return new BasicResponse(HttpStatus.NOT_ACCEPTABLE.value(), "오늘의 tmi를 작성하지 않았습니다.");
        }
        return new BasicResponse(HttpStatus.OK.value(), "오늘의 tmi를 작성했습니다.");
    }
    public BasicResponse registerTmi(TmiDto tmiDto) {
        Member member = securityUtil.extractMember();
//        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//        Long count = tmiRepository.countByMemberAndCreateAtIsAfterAndCreateAtIsBefore(member, startOfDay, endOfDay);
//        if (count >= MAX_TMI_COUNT_PER_DAY)
//            return new BasicResponse(HttpStatus.NOT_ACCEPTABLE.value(), "TMI는 하루에 3개까지 입력할 수 있습니다.");

        Tmi tmi = new Tmi(tmiDto, member);
        tmiRepository.save(tmi);
        return new BasicResponse(HttpStatus.CREATED.value(), "Tmi 등록 완료");
    }

    public List<ChatResponseDto> getTmiByFamilyId() {
        Long familyId = securityUtil.extractFamilyId();
        System.out.println("familyId = " + familyId);
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.out.println("today = " + today);
        return tmiRepository.findByMember_Family_FamilyIdAndCreateAtIsAfterOrderByCreateAtDesc(familyId, today)
                        .stream()
                        .map(Tmi::toDto)
                        .collect(Collectors.toList());

//        System.out.println("tmis = " + tmis);
//        return tmis;
    }

    public BasicResponse attend() {
        Member member = securityUtil.extractMember();

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        if (!attendanceRepository.findByMemberAndAttendanceTimeIsAfterAndAttendanceTimeIsBefore(member, startOfDay, endOfDay).isEmpty()) {
            return new BasicResponse(HttpStatus.NOT_ACCEPTABLE.value(), "출석은 하루에 한번만 가능합니다");
        }

        Attendance attendance = new Attendance(member, true);
        attendanceRepository.save(attendance);

        return new BasicResponse(HttpStatus.OK.value(), "출석 완료!");
    }

    public ExtendedResponse<List<Attendance>> getAttendees() {
        Long familyId = securityUtil.extractFamilyId();
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        List<Attendance> attendances = attendanceRepository.findByMember_Family_FamilyIdAndAttendanceTimeIsAfter(familyId, today);
        return new ExtendedResponse<>(attendances, HttpStatus.OK.value(), "");
    }

}
