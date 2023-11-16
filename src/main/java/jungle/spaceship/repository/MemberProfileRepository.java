package jungle.spaceship.repository;

import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
