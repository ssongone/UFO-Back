package jungle.spaceship.photo.repository;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.photo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByMember(Member member);
}
