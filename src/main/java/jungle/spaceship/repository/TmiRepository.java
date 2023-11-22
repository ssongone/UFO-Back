package jungle.spaceship.repository;

import jungle.spaceship.entity.Tmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TmiRepository extends JpaRepository<Tmi, Long> {
}
