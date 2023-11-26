package jungle.spaceship.member.controller.dto;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.entity.family.Family;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FamilyResponseDto {
    private Long familyId;
    private Long chatRoomId;
    private String ufoName;
    private Plant plant;
    private List<Member> members;

    public FamilyResponseDto(Family family) {
        this.familyId = family.getFamilyId();
        this.chatRoomId = family.getChatRoom().getRoomId();
        this.ufoName = family.getUfoName();
        this.plant = family.getPlant();
        this.members = family.getMembers();
    }
}
