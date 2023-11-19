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
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Builder
    public Member(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

//    public String getRoleKey(){
//        return this.role.getKey();
//    }
    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }

}
