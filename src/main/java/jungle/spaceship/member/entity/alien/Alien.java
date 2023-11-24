package jungle.spaceship.member.entity.alien;


import jungle.spaceship.member.controller.dto.AlienDto;
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

    @Column
    private AlienType type;
    @Column
    private AlienColor color;

    public Alien(AlienDto dto) {
        this.type = dto.getType();
        this.color = dto.getColor();
    }
}
