package jungle.spaceship.photo.repository;

import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.entity.FamilyRoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRoleInfoRepository extends JpaRepository<FamilyRoleInfo, Long> {
    FamilyRoleInfo findByFamilyRole(FamilyRole familyRole);
}
