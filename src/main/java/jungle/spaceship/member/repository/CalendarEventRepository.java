package jungle.spaceship.member.repository;

import jungle.spaceship.member.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    List<CalendarEvent> findByMember_Family_FamilyIdAndEndDateIsAfterAndStartDateIsBefore(Long familyId, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
