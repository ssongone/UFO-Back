package jungle.spaceship.photo.repository;

import jungle.spaceship.photo.entity.PhotoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, Long> {

    @Query(value = "SELECT pt.* FROM photo_tag AS pt " +
            "INNER JOIN (SELECT p.photo_id FROM photo p WHERE p.photo_id < :lastPhotoId ORDER BY p.create_at DESC LIMIT :photo_cnt) AS pp " +
            "ON pt.photo_id = pp.photo_id " +
            "WHERE pt.family_id = :familyId ORDER BY pp.photo_id DESC", nativeQuery = true)
    List<PhotoTag> findRecentPhotoTagsWithPaging(
            @Param("photo_cnt") int photo_cnt,
            @Param("familyId") Long familyId,
            @Param("lastPhotoId") Long lastPhotoId);

    @Query(value = "SELECT pt.* FROM photo_tag AS pt " +
            "INNER JOIN (SELECT p.photo_id FROM photo p ORDER BY p.create_at DESC LIMIT :photo_cnt) AS pp " +
            "ON pt.photo_id = pp.photo_id " +
            "WHERE pt.family_id = :familyId ORDER BY pp.photo_id DESC", nativeQuery = true)
    List<PhotoTag> findRecentPhotoTags(
            @Param("photo_cnt") int photo_cnt,
            @Param("familyId") Long familyId
    );


    @Query(value = "SELECT pt.* FROM photo_tag AS pt " +
            "INNER JOIN (SELECT p.photo_id FROM photo p WHERE p.photo_id < :lastPhotoId ORDER BY p.create_at DESC LIMIT :photo_cnt) AS pp " +
            "ON pt.photo_id = pp.photo_id " +
            "WHERE pt.family_id = :familyId " +
            "AND pt.role_id = :roleId " +
            "ORDER BY pp.photo_id DESC", nativeQuery = true)
    List<PhotoTag> findRecentPhotoTagsByFamilyRoleWithPaging(
            @Param("photo_cnt") int photo_cnt,
            @Param("familyId") Long familyId,
            @Param("lastPhotoId") Long lastPhotoId,
            @Param("roleId") Long roleId
    );

    @Query(value = "SELECT pt.* FROM photo_tag AS pt " +
            "INNER JOIN (SELECT p.photo_id FROM photo p ORDER BY p.create_at DESC LIMIT :photo_cnt) AS pp " +
            "ON pt.photo_id = pp.photo_id " +
            "WHERE pt.family_id = :familyId " +
            "AND pt.role_id = :roleId " +
            "ORDER BY pp.photo_id DESC", nativeQuery = true)
    List<PhotoTag> findRecentPhotoTagsByFamilyRole(
            @Param("photo_cnt") int photo_cnt,
            @Param("familyId") Long familyId,
            @Param("roleId") Long roleId
    );



}
