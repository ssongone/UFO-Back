package jungle.spaceship.photo.repository;

import jungle.spaceship.photo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
