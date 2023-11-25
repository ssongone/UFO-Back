package jungle.spaceship.photo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long photoId;

    private String photoName;

    private String description;                                   /* 사진 간단 설명 */


    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<PhotoTag> photoTags = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 사진 등록자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public Photo(String fileName, String description, Member member, Family family){
        this.photoName = fileName;
        this.description = description;
        this.member = member;
        this.family = family;
    }



    public void toPhotoTag(FamilyRoleInfo roleInfo) {
        this.photoTags.add(
                new PhotoTag(roleInfo, this)
        );

    }

}
