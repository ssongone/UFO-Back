package jungle.spaceship.photo.controller.dto;

import jungle.spaceship.member.entity.family.FamilyRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@Getter
@RequiredArgsConstructor
public class PhotoListRequestDto {

    @Nullable
    private final Long photoId;

    private FamilyRole familyRole;
}
