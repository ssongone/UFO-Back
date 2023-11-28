package jungle.spaceship.member.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jungle.spaceship.member.controller.dto.SignUpDto;
import jungle.spaceship.member.entity.alien.Alien;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.Role;
import jungle.spaceship.member.entity.oauth.OAuthInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String nickname;

    private String familyRole;

    private LocalDate birthdate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "alien_id")
    private Alien alien;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonBackReference
    private Family family;

    @JsonIgnore
    private String firebaseToken;
    public void setFamily(Family family) {
        this.family = family;
    }

    @Builder
    public Member(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public Member(OAuthInfoResponse oAuthInfoResponse) {
        this.name = oAuthInfoResponse.getName();
        this.email = oAuthInfoResponse.getEmail();
        this.picture = oAuthInfoResponse.getPicture();
        this.role = Role.GUEST;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public Member update(SignUpDto dto){
        this.nickname = dto.getNickname();
        this.familyRole = dto.getFamilyRole();
        this.birthdate = dto.getBirthdate();
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAlien(Alien alien) {
        this.alien = alien;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
