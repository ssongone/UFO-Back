package jungle.spaceship.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jungle.spaceship.controller.dto.SignUpDto;
import jungle.spaceship.entity.oauth.OAuthInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
@NoArgsConstructor
@Entity
public class Member extends Timestamped implements UserDetails {

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

    private String title;

    private LocalDate birthdate;

    @OneToOne
    @JoinColumn(name = "alien_id")
    private Alien alien;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonBackReference
    private Family family;

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
        this.title = dto.getTitle();
        this.birthdate = dto.getBirthdate();
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAlien(Alien alien) {
        this.alien = alien;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.memberId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
