package jungle.spaceship.member.repository;

import jungle.spaceship.member.entity.family.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
}
