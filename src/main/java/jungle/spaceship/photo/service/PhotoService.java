package jungle.spaceship.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.member.repository.FamilyRepository;
import jungle.spaceship.photo.controller.dto.*;
import jungle.spaceship.photo.entity.PhotoTag;
import jungle.spaceship.photo.repository.PhotoRepository;
import jungle.spaceship.photo.repository.FamilyRoleInfoRepository;
import jungle.spaceship.photo.entity.Photo;
import jungle.spaceship.photo.entity.FamilyRoleInfo;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final SecurityUtil securityUtil;
    private final FamilyRoleInfoRepository roleRepository;
    private final PhotoRepository photoRepository;
    private final PhotoTagRepository photoTagRepository;
    private final FamilyRepository familyRepository;
    private final FamilyRoleInfoRepository familyRoleInfoRepository;

    private final static int PHOTO_PAGEABLE_CNT = 40;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Transactional
    public BasicResponse registerPhoto(PhotoRegisterDto photoDto) {
        // 작성자
        Member member = securityUtil.extractMember();

        // 사진
        Photo photo = photoDto.toPhoto(member);

        for(FamilyRole writer : photoDto.photoTags()){

            // FamilyRole Info 클래스 찾음
            FamilyRoleInfo roleInfo = roleRepository.findByFamilyRole(writer);

            // PhotoTag 저장
            photo.toPhotoTag(roleInfo, member.getFamily());
        }

        photoRepository.save(photo);
        PhotoResponseDto photoResponseDto =
                new PhotoResponseDto(photo.getPhotoId(), photo.getCreateAt());

        return new ExtendedResponse<>(photoResponseDto,HttpStatus.CREATED.value(), "사진이 생성되었습니다");
    }

    @Transactional
    public BasicResponse getPhotoList(Long familyId, Long photoId){


        if( ! isValidFamilyId(familyId)){
            throw new NoSuchElementException("해당하는 가족이 없습니다");
        }

        List<PhotoTag> photoTags;
        if(photoId == null){
            // 최신 사진 페이징 처리
            photoTags = photoTagRepository.findRecentPhotoTags(PHOTO_PAGEABLE_CNT, familyId);
        }else{
            // photoId 기준 최신 사진 페이지 처리
            photoTags = photoTagRepository.findRecentPhotoTagsWithPaging(PHOTO_PAGEABLE_CNT, familyId, photoId);
        }

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 리스트 반환 성공!");

    }

    @Transactional
    public BasicResponse getPhotoListByTag(PhotoListRequestDto requestDto){

        Long familyId = requestDto.getFamilyId();
        Long lastPhotoId = requestDto.getPhotoId();
        FamilyRole familyRole = requestDto.getFamilyRole();

        if( ! isValidFamilyId(familyId)){
            throw new NoSuchElementException("해당하는 가족이 없습니다");
        }
        Long roleId = familyRoleInfoRepository.findByFamilyRole(familyRole).getFamilyRoleId();

        List<PhotoTag> photoTags;
        if(lastPhotoId == null){
            photoTags = photoTagRepository.findRecentPhotoTagsByFamilyRole(PHOTO_PAGEABLE_CNT, familyId, roleId);
        }else{
            photoTags = photoTagRepository.findRecentPhotoTagsByFamilyRoleWithPaging(PHOTO_PAGEABLE_CNT, familyId, lastPhotoId, roleId);
        }

        List<PhotoListResponseDto> result = getPhotoListResponse(photoTags);

        return new ExtendedResponse<>(result,HttpStatus.OK.value(), "사진 리스트 반환 성공!");
    }

    private List<PhotoListResponseDto> getPhotoListResponse(List<PhotoTag> photoTags) {
        Map<Long, PhotoListResponseDto> photoResponseMap = new HashMap<>();

        for (PhotoTag photoTag : photoTags) {
            Long photoId = photoTag.getPhoto().getPhotoId();

            // 이미 생성된 PhotoListResponseDto 가 있는지 확인
            PhotoListResponseDto responseDto =
                    photoResponseMap.computeIfAbsent(photoId, k ->
                            new PhotoListResponseDto(
                                    photoTag.getPhoto().getPhotoId(),
                                    makeS3Url(photoTag.getPhoto().getPhotoKey()),
                                    photoTag.getPhoto().getCreateAt())
                    );
            // FamilyRole 을 추가
            responseDto.setFamilyRole(photoTag.getFamilyRoleInfo().getFamilyRole());
        }

        return new ArrayList<>(photoResponseMap.values());
    }

    // s3에 업로드 된 이미지의 객체 url 생성
    private String makeS3Url(String photoKey){
        return amazonS3.getUrl(bucket, photoKey).toString();
    }

}
