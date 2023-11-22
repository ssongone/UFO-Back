package jungle.spaceship.repository.mole;

import jungle.spaceship.entity.mole.MoleGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoleGameRepository extends JpaRepository<MoleGame, Long> {
}
