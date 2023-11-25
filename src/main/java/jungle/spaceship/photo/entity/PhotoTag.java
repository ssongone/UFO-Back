package jungle.spaceship.photo.entity;

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

    public PhotoTag(FamilyRoleInfo roleInfo, Photo photo){
        this.familyRoleInfo = roleInfo;
        this.photo = photo;
    }

}
