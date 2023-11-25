package jungle.spaceship.photo.controller.dto;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.entity.Photo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PhotoRegisterDto {

    private final String fileName;

    private final List<FamilyRole> photoTags;

    private final String description;

    public Photo toPhoto(Member member, Family family){
        return new Photo(fileName, description, member, family);
    }

}
