package jungle.spaceship.entity;


import jungle.spaceship.entity.auth.OAuthInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

//    private String authId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @OneToOne
    @JoinColumn(name = "member_profile_id", nullable = true)
    private MemberProfile memberProfile;

    @Builder
    public Member(String name, String email, String picture, Role role, String authId){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
//        this.authId = authId;
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
    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }
}
