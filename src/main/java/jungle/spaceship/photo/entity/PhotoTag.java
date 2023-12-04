package jungle.spaceship.photo.entity;

import jungle.spaceship.member.entity.family.Family;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PhotoTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private FamilyRoleInfo familyRoleInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public PhotoTag(FamilyRoleInfo roleInfo, Family family, Photo photo){
        this.familyRoleInfo = roleInfo;
        this.family = family;
        this.photo = photo;
    }

    public PhotoTag(Family family, Photo photo) {
        this.familyRoleInfo = null;
        this.family = family;
        this.photo = photo;
    }
}
