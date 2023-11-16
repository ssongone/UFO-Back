package jungle.spaceship.entity;

import jungle.spaceship.controller.dto.SignUpDto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class MemberProfile extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberProfileId;

    @Column(nullable = false)
    private String nickname;

    private String title;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private ElienType elienType;

    public MemberProfile(SignUpDto signUpDto) {
        this.nickname = signUpDto.getNickname();
        this.title = signUpDto.getTitle();
        this.birthdate = signUpDto.getBirthdate();
        this.elienType = signUpDto.getElienType();
    }
}
