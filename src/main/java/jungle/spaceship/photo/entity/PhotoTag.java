package jungle.spaceship.photo.entity;

import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
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

    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public PhotoTag(FamilyRole familyRole, Family family, Photo photo){
        this.familyRole = familyRole;
        this.family = family;
        this.photo = photo;
    }

    public PhotoTag(Family family, Photo photo) {
        this.familyRole = null;
        this.family = family;
        this.photo = photo;
    }
}
