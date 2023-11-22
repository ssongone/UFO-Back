package jungle.spaceship.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Tmi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tmiId;

    private String myTmi;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Tmi(String content, Member member) {
        this.content = content;
        this.member = member;
    }
}
