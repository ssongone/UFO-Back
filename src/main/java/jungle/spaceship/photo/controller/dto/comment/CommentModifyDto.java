package jungle.spaceship.photo.controller.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentModifyDto{

    private String content;

    @Setter
    private LocalDateTime modifiedAt;

}
