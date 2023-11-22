package jungle.spaceship.entity;

import jungle.spaceship.controller.dto.TmiDto;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Tmi extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tmiId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Tmi(TmiDto dto, Member member) {
        this.content = dto.getContent();
        this.member = member;
    }
}
