package jungle.spaceship.photo.service;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.service.PlantService;
import jungle.spaceship.photo.controller.dto.comment.CommentModifyDto;
import jungle.spaceship.photo.controller.dto.comment.CommentRegisterDto;
import jungle.spaceship.photo.controller.dto.comment.CommentResponseDto;
import jungle.spaceship.photo.entity.Comment;
import jungle.spaceship.photo.entity.Photo;
import jungle.spaceship.photo.repository.CommentRepository;
import jungle.spaceship.photo.repository.PhotoRepository;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static jungle.spaceship.member.entity.Plant.ADD_COMMENT_POINT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final SecurityUtil securityUtil;
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final PlantService plantService;
    /**
     * 댓글 등록
     */
    @Transactional
    public BasicResponse registerComment(CommentRegisterDto commentRegisterDto) {
        Member member = securityUtil.extractMember();

        Photo photo =
                photoRepository.findById(commentRegisterDto.photoId())
                        .orElseThrow(() -> new NoSuchElementException("해당하는 사진이 없습니다"));

        Comment comment = commentRegisterDto.toComment(photo, member);
        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        plantService.performActivity(member, ADD_COMMENT_POINT);

        return new ExtendedResponse<>(commentResponseDto, HttpStatus.CREATED.value(), "댓글이 생성되었습니다");
    }

    /**
     * 댓글 수정
     */
    public BasicResponse modifyComment(Long commentId, CommentModifyDto commentModifyDto) {

        String content = commentModifyDto.getContent();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 댓글이 없습니다"));

        // 댓글 수정
        comment.setContent(content);
        commentModifyDto.setModifiedAt(comment.getModifiedAt());
        commentRepository.save(comment);

        return new ExtendedResponse<>(commentModifyDto, HttpStatus.OK.value(), "댓글이 수정 완료!");
    }

    /**
     * 댓글 삭제
     */
    public BasicResponse deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 댓글이 없습니다"));

        commentRepository.deleteById(commentId);
        return new BasicResponse(HttpStatus.NO_CONTENT.value(), "댓글 삭제 완료!");

    }
}
