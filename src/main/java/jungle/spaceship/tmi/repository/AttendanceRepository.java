package jungle.spaceship.tmi.repository;

import jungle.spaceship.tmi.entity.Attendance;
import jungle.spaceship.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

     List<Attendance> findByMemberAndAttendanceTimeIsAfterAndAttendanceTimeIsBefore(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay);
     List<Attendance> findByMember_Family_FamilyIdAndAttendanceTimeIsAfter(Long familyId, LocalDateTime today);

     @Query("SELECT DATE(a.attendanceTime) as date, a FROM Attendance a " +
             "JOIN a.member m " +
             "JOIN m.family f " +
             "WHERE f.familyId = :familyId " +
             "AND a.attendanceTime > :createdAt " +
             "ORDER BY a.attendanceTime DESC"
     )
     List<Object[]> findAttendanceTimeByFamilyAndDate(@Param("familyId") Long familyId, @Param("createdAt") LocalDateTime createdAt);
    void deleteByMember(Member member);

}


