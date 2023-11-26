package jungle.spaceship.photo.controller.dto;

import jungle.spaceship.photo.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CommentResponseDto {
    private final Long commentId;

    private final String content;

    private final LocalDateTime createAt;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createAt = comment.getCreateAt();
    }

}
