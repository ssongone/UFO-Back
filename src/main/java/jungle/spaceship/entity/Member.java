package jungle.spaceship.entity;


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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    private String authId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Builder
    public Member(String name, String email, String picture, Role role, String authId){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.authId = authId;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }

}
