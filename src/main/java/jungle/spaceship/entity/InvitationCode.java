package jungle.spaceship.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor

public class InvitationCode {

    public static final String CODE_CHARACTERS = "!@#$%^&*ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final int CODE_LENGTH = 32;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codeId;

    private String code;
    @OneToOne
    @JoinColumn(name = "family_id")
    private Family family;

    public InvitationCode(String code, Family family) {
        this.code = code;
        this.family = family;
    }
}
