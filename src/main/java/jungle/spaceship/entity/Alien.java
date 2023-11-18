package jungle.spaceship.entity;


import jungle.spaceship.controller.dto.AlienDto;
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
    private ElienType type;
    @Column
    private AlienColor color;

    public Alien(AlienDto dto) {
        this.type = dto.getType();
        this.color = dto.getColor();
    }
}
