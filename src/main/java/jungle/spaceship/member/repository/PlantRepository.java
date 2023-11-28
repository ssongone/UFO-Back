package jungle.spaceship.member.repository;

import jungle.spaceship.member.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
