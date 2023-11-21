package jungle.spaceship.entity.mole;

import jungle.spaceship.entity.Family;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MoleGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long GameId;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    private LocalDateTime startAt;

}
