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

    @OneToOne(mappedBy = "member")
    private Member member;

    private ElienType type;
    private AlienColor color;

    public Alien(Member member, AlienDto dto) {
        this.member = member;
        this.type = dto.getType();
        this.color = dto.getColor();
    }
}
