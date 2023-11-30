package jungle.spaceship.member.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.messaging.Notification;
import jungle.spaceship.member.controller.dto.CharacterDto;
import jungle.spaceship.member.controller.dto.SignUpDto;
import jungle.spaceship.member.entity.alien.Alien;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.member.entity.family.Role;
import jungle.spaceship.member.entity.oauth.OAuthInfoResponse;
import jungle.spaceship.notification.PushAlarm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Member extends Timestamped implements PushAlarm {

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
    @JsonIgnore
    private Role role;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole;

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

    public void update(SignUpDto dto){
        this.nickname = dto.getNickname();
        this.birthdate = dto.getBirthdate();
    }

    public void updateCharacter(CharacterDto dto) {
        setNickname(dto.getNickname());
        setBirthdate(dto.getBirthdate());
        this.getAlien().setType(dto.getAlienType());
        setFamilyRole(dto.getFamilyRole());
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setFamilyRole(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

    public void setAlien(Alien alien) {
        this.alien = alien;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle("찌릿찌릿 " + this.nickname + "님이 프로필을 수정했어요")
                .setBody(DEFAULT_BODY).build();
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();

        additionalData.put("members", this.family.getMembers().toString());

        return additionalData;
    }
}
