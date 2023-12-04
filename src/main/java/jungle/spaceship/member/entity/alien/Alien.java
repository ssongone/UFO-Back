package jungle.spaceship.member.entity.alien;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Alien {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long alienId;

    @Enumerated(EnumType.STRING)
    private AlienType type;

    @JsonIgnore
    private AlienColor color;

    public Alien(AlienType type) {
        this.type = type;
    }

    public void setType(AlienType type) {
        this.type = type;
    }
}
