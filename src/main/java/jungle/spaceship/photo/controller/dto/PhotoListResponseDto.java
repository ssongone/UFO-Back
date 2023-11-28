package jungle.spaceship.photo.controller.dto;

import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.entity.Photo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@RequiredArgsConstructor
public class PhotoListResponseDto {
    private Long photoId;
    private String photoKey;
    private LocalDateTime createAt;

    private final List<FamilyRole> photoTags = new ArrayList<>();


    public void setPhoto(Photo photo) {
        this.photoId = photo.getPhotoId();
        this.photoKey = photo.getPhotoKey();
    }

    public void setFamilyRole(FamilyRole familyRole) {
        this.photoTags.add(familyRole);
    }

    public PhotoListResponseDto(Long photoId, String photoKey, LocalDateTime createAt){
        this.photoId = photoId;
        this.photoKey = photoKey;
        this.createAt = createAt;
    }
}
