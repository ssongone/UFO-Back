package jungle.spaceship.member.repository;

import jungle.spaceship.member.entity.alien.Alien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlienRepository extends JpaRepository<Alien, Long> {
}
