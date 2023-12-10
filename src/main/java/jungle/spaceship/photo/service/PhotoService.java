package jungle.spaceship.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.member.service.PlantService;
import jungle.spaceship.notification.FcmService;
import jungle.spaceship.notification.NotificationType;
import jungle.spaceship.photo.controller.dto.PhotoListResponseDto;
import jungle.spaceship.photo.controller.dto.PhotoRegisterDto;
import jungle.spaceship.photo.controller.dto.PhotoResponseDto;
import jungle.spaceship.photo.controller.dto.PhotoTagRequestDto;
import jungle.spaceship.photo.controller.dto.comment.CommentResponseDto;
import jungle.spaceship.photo.entity.Comment;
import jungle.spaceship.photo.entity.Photo;
import jungle.spaceship.photo.entity.PhotoTag;
import jungle.spaceship.photo.repository.PhotoRepository;
import jungle.spaceship.photo.repository.PhotoTagRepository;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static jungle.spaceship.member.entity.Plant.UPLOAD_PHOTO_POINT;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final SecurityUtil securityUtil;
    private final PhotoRepository photoRepository;
    private final PhotoTagRepository photoTagRepository;
    private final FcmService fcmService;
    private final PlantService plantService;
    private final static int PHOTO_PAGEABLE_CNT = 80;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Transactional
    public BasicResponse registerPhoto(PhotoRegisterDto photoDto) {
        // 작성자
        Member member = securityUtil.extractMember();

        // 사진
        Photo photo = photoDto.toPhoto(member);

        if(photoDto.photoTags().isEmpty()){
            photo.toNoneTag(member.getFamily());

        }else{
            for(String writer : photoDto.photoTags()){

                FamilyRole byRoleName = FamilyRole.findByRoleName(writer);
                photo.toPhotoTag(byRoleName, member.getFamily());
            }
        }

        photoRepository.save(photo);
        PhotoResponseDto photoResponseDto =
                new PhotoResponseDto(photo.getPhotoId(), photo.getCreateAt());
        fcmService.sendFcmMessageToFamilyExcludingMe(member, NotificationType.PHOTO, photoDto.description());
        plantService.performActivity(member, UPLOAD_PHOTO_POINT);
        return new ExtendedResponse<>(photoResponseDto,HttpStatus.CREATED.value(), "사진이 생성되었습니다");
    }

    @Transactional
    public BasicResponse getPhotoList(){
        Long familyId = securityUtil.extractFamilyId();

        // 최신 사진 페이징 처리
        List<PhotoTag> photoTags =
                photoTagRepository.findRecentPhotoTags(PHOTO_PAGEABLE_CNT, familyId);

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 리스트 반환 성공!");
    }

    @Transactional
    public BasicResponse getPhotoListById(Long photoId) {
        Long familyId = securityUtil.extractFamilyId();

        // photoId 기준 최신 사진 페이지 처리
        List<PhotoTag> photoTags =
                photoTagRepository.findRecentPhotoTagsWithPaging(PHOTO_PAGEABLE_CNT, familyId, photoId);

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 리스트 반환 성공!");
    }

    @Transactional
    public BasicResponse getPhotoListByTag(PhotoTagRequestDto requestDto){

        Long familyId = securityUtil.extractFamilyId();

        FamilyRole familyRole = requestDto.familyRole();

//        Long roleId = familyRoleInfoRepository.findByFamilyRole(familyRole).getFamilyRoleId();

        List<PhotoTag> photoTags =
                photoTagRepository.findRecentPhotoTagsByFamilyRole(PHOTO_PAGEABLE_CNT, familyId, familyRole);

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 태그 리스트 반환 성공!");
    }

    public BasicResponse getPhotoListByTagAndId(Long photoId, PhotoTagRequestDto photoTagRequestDto) {

        Long familyId = securityUtil.extractFamilyId();
        FamilyRole familyRole = photoTagRequestDto.familyRole();

//        Long roleId = familyRoleInfoRepository.findByFamilyRole(familyRole).getFamilyRoleId();

        List<PhotoTag> photoTags =
                photoTagRepository.findRecentPhotoTagsByFamilyRoleWithPaging(PHOTO_PAGEABLE_CNT, familyId, photoId, familyRole);

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 태그 리스트 반환 성공!");
    }


    private List<PhotoListResponseDto> getPhotoListResponse(List<PhotoTag> photoTags) {
        Map<Long, PhotoListResponseDto> photoResponseMap = new HashMap<>();

        for (PhotoTag photoTag : photoTags) {
            Long photoId = photoTag.getPhoto().getPhotoId();

            // 이미 생성된 PhotoListResponseDto 가 있는지 확인
            PhotoListResponseDto responseDto =
                    photoResponseMap.computeIfAbsent(photoId, k ->
                            new PhotoListResponseDto(
                                    photoTag.getPhoto(),
                                    makeS3Url(photoTag.getPhoto().getPhotoKey()))
                    );
            // FamilyRole 을 추가
            if(photoTag.getFamilyRole() != null){
                responseDto.setFamilyRole(photoTag.getFamilyRole().getRoleName());
            }
        }

        return new ArrayList<>(photoResponseMap.values());
    }


    // s3에 업로드 된 이미지의 객체 url 생성
    private String makeS3Url(String photoKey){
        return amazonS3.getUrl(bucket, photoKey).toString();
    }

    public BasicResponse getPhotoComments(Long photoId) {
        Optional<Photo> byId = photoRepository.findById(photoId);
        if (byId.isEmpty()) {
            return new ExtendedResponse<>(null, HttpStatus.BAD_REQUEST.value(), "해당 사진이 없슴다");
        }
        Photo photo = byId.get();
        List<Comment> comments = photo.getComment();
        List<CommentResponseDto> collect = comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
        return new ExtendedResponse<>(collect, HttpStatus.OK.value(), "댓글 리스트 반환 성공!");
    }


}
