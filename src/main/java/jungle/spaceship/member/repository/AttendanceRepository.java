package jungle.spaceship.member.repository;

import jungle.spaceship.tmi.entity.Attendance;
import jungle.spaceship.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

     List<Attendance> findByMemberAndAttendanceTimeIsAfterAndAttendanceTimeIsBefore(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay);
     List<Attendance> findByMember_Family_FamilyIdAndAttendanceTimeIsAfter(Long familyId, LocalDateTime today);
}
