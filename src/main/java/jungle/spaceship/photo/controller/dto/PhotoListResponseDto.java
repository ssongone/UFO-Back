package jungle.spaceship.photo.controller.dto;

import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.entity.Comment;
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
    private String description;
    private String writer;
    private List<Comment> comments;
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

    public PhotoListResponseDto(Photo photo, String photoKey){

        this.photoId = photo.getPhotoId();
        this.description = photo.getDescription();
        this.writer = photo.getMember().getNickname();
        this.comments = photo.getComment();
        this.photoKey = photoKey;
        this.createAt = photo.getCreateAt();
    }
}
