package jungle.spaceship.repository;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Tmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TmiRepository extends JpaRepository<Tmi, Long> {
    List<Tmi> findByMember_Family_FamilyIdAndCreateAtAfterOrderByCreateAtDesc(Long familyId, LocalDateTime today);

    Long countByMemberAndCreateAtAfterAndCreateAtBefore(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
