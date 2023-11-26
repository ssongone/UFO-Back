package jungle.spaceship.photo.controller;

import jungle.spaceship.photo.controller.dto.CommentModifyDto;
import jungle.spaceship.photo.controller.dto.CommentRegisterDto;
import jungle.spaceship.photo.service.CommentService;
import jungle.spaceship.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BasicResponse> registerComment(@RequestBody CommentRegisterDto commentRegisterDto){
        return ResponseEntity.ok(commentService.registerComment(commentRegisterDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<BasicResponse> modifyComment(@RequestBody CommentModifyDto commentModifyDto,
                                                       @PathVariable Long commentId){
        log.info(commentModifyDto.getContent());
        return ResponseEntity.ok(commentService.modifyComment(commentId, commentModifyDto));
    }

}
