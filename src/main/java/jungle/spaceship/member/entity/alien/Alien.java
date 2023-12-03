package jungle.spaceship.member.entity.alien;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jungle.spaceship.member.controller.dto.AlienDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@ToString
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

    public Alien(AlienDto dto) {
        this.type = dto.getType();
//        this.color = dto.getColor();
    }

    public void setType(AlienType type) {
        this.type = type;
    }
}
