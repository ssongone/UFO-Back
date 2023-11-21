package jungle.spaceship.entity.mole;

import jungle.spaceship.entity.Member;

import javax.persistence.*;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scoreId;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private MoleGame moleGame;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int score;

}
