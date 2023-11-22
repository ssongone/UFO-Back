package jungle.spaceship.controller.dto;

import jungle.spaceship.entity.Family;
import jungle.spaceship.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FamilyResponseDto {
    private Long familyId;
    private String ufoName;
    private String plantName;
    private List<Member> members;

    public FamilyResponseDto(Family family) {
        this.familyId = family.getFamilyId();
        this.ufoName = family.getUfoName();
        this.plantName = family.getPlantName();
        this.members = family.getMembers();
    }
}
