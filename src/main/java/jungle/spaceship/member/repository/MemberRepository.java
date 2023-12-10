package jungle.spaceship.member.repository;

import jungle.spaceship.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberId(Long memberId);


    List<Member> findByFamily_FamilyIdOrderByPointDesc(Long familyId);
}
