package jungle.spaceship.tmi.repository;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.tmi.entity.Tmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TmiRepository extends JpaRepository<Tmi, Long> {
    List<Tmi> findByMember_Family_FamilyIdAndCreateAtIsAfterOrderByCreateAtDesc(Long familyId, LocalDateTime today);

    Long countByMemberAndCreateAtIsAfterAndCreateAtIsBefore(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT DATE(t.createAt) as date, t FROM Tmi t " +
            "JOIN t.member m " +
            "JOIN m.family f " +
            "WHERE f.familyId = :familyId " +
            "AND t.createAt > :createdAt " +
            "ORDER BY t.createAt DESC"
    )
    List<Object[]> findTmiDataByFamilyAndDate(@Param("familyId") Long familyId, @Param("createdAt") LocalDateTime createdAt);

    void deleteByMember(Member member);


}
