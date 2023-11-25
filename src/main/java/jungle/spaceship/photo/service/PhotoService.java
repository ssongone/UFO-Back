package jungle.spaceship.photo.service;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.controller.dto.PhotoResponseDto;
import jungle.spaceship.photo.repository.PhotoRepository;
import jungle.spaceship.photo.repository.FamilyRoleInfoRepository;
import jungle.spaceship.photo.controller.dto.PhotoRegisterDto;
import jungle.spaceship.photo.entity.Photo;
import jungle.spaceship.photo.entity.FamilyRoleInfo;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final SecurityUtil securityUtil;
    private final FamilyRoleInfoRepository roleRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    public BasicResponse registerPhoto(PhotoRegisterDto photoDto) {
        // 작성자
        Member member = securityUtil.extractMember();

        // 사진
        Photo photo = photoDto.toPhoto( member,
                                        member.getFamily());

        for(FamilyRole writer : photoDto.getPhotoTags()){

            // FamilyRole Info 클래스 찾음
            FamilyRoleInfo roleInfo = roleRepository.findByFamilyRole(writer);

            // PhotoTag 저장
            photo.toPhotoTag(roleInfo);
        }

        photoRepository.save(photo);
        PhotoResponseDto photoResponseDto =
                new PhotoResponseDto(photo.getPhotoId(), photo.getCreateAt());

        return new ExtendedResponse<>(photoResponseDto,HttpStatus.CREATED.value(), "가족이 생성되었습니다");
    }

}
