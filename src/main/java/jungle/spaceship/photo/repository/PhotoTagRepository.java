package jungle.spaceship.photo.repository;

import jungle.spaceship.photo.entity.PhotoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, Long> {
}
