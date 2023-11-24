package jungle.spaceship.member.entity.family;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jungle.spaceship.member.controller.dto.FamilyDto;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Timestamped;
import jungle.spaceship.chat.entity.ChatRoom;
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
    private String plantName;

    @OneToMany(mappedBy = "family")
    @JsonManagedReference
    private List<Member> members = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    public Family(FamilyDto dto, ChatRoom chatRoom) {
        this.ufoName = dto.getUfoName();
        this.plantName = dto.getPlantName();
        this.chatRoom = chatRoom;
    }

}
