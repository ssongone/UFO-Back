package jungle.spaceship.photo.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.photo.entity.Comment;
import jungle.spaceship.photo.entity.Photo;


public record CommentRegisterDto(
        @JsonProperty("photoId") Long photoId,
        @JsonProperty("content") String content)
{

    public Comment toComment(Photo photo, Member member) {
        System.out.println("content = " + content);
        return new Comment(
                content, photo, member);
    }

}
