package jungle.spaceship.photo.entity;

import jungle.spaceship.member.entity.family.FamilyRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "familyRole_uq_familyRole_name",
                columnNames = {"familyRole"})
)
public class FamilyRoleInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyRoleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyRole familyRole;


    public FamilyRoleInfo(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

}
