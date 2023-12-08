package jungle.spaceship.photo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Timestamped;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.controller.dto.comment.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Photo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long photoId;

    private String description;                                   /* 사진 간단 설명 */

    private String photoKey;

    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<PhotoTag> photoTags = new LinkedHashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 사진 등록자

    @OneToMany(mappedBy = "photo",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Comment> comment = new ArrayList<>();            /* 사진 댓글 */

    public Photo(String description, String photoKey, Member member){
        this.description = description;
        this.photoKey = photoKey;
        this.member = member;
    }

    public void toPhotoTag(FamilyRole familyRole, Family family) {
        this.photoTags.add(
                new PhotoTag(familyRole, family, this)
        );

    }

    public void update(String description) {
        this.description = description;
    }

    public void toNoneTag(Family family) {
        this.photoTags.add(
                new PhotoTag(family, this)
        );
    }

    public List<CommentResponseDto> dtoComments() {
        return comment.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
