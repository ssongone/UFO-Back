package jungle.spaceship.photo.repository;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    void deleteByMember(Member member);
}
