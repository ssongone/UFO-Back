package jungle.spaceship.member.entity.family;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jungle.spaceship.chat.entity.ChatRoom;
import jungle.spaceship.member.controller.dto.FamilyDto;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Family extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long familyId;

    private String ufoName;

    @OneToMany(mappedBy = "family")
    @JsonManagedReference
    private List<Member> members = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    public Family(FamilyDto dto, ChatRoom chatRoom, Plant plant) {
        this.ufoName = dto.getUfoName();
        this.chatRoom = chatRoom;
        this.plant = plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
