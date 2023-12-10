package jungle.spaceship.photo.controller;

import jungle.spaceship.photo.controller.dto.PhotoTagRequestDto;
import jungle.spaceship.photo.controller.dto.PhotoRegisterDto;
import jungle.spaceship.photo.service.PhotoService;
import jungle.spaceship.photo.controller.dto.S3RegisterDto;
import jungle.spaceship.photo.service.S3Service;
import jungle.spaceship.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/photo")
@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final S3Service s3Service;

    @PostMapping("/s3")
    public ResponseEntity<String> createPreSignedUrl(@RequestBody S3RegisterDto s3RegisterDto)
    {
        log.info(s3RegisterDto.fileName());
        log.info(s3RegisterDto.prefix());
        return ResponseEntity.ok(s3Service.getPreSignedUrl(s3RegisterDto));
    }

    @PostMapping
    public ResponseEntity<BasicResponse> registerPhoto(@RequestBody PhotoRegisterDto photoDto)
    {
        log.info(photoDto.photoTags().toString());
        log.info(photoDto.description());
        log.info(photoDto.photoKey());
        return ResponseEntity.ok(photoService.registerPhoto(photoDto));
    }

    @GetMapping
    public ResponseEntity<BasicResponse> getPhotoList()
    {
        return ResponseEntity.ok(photoService.getPhotoList());
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<BasicResponse> getPhotoListById(@PathVariable("photoId")Long photoId)
    {
        return ResponseEntity.ok(photoService.getPhotoListById(photoId));
    }

    @PostMapping("/tag")
    public ResponseEntity<BasicResponse> getPhotoListByTag(@RequestBody PhotoTagRequestDto photoTagRequestDto)
    {
        return ResponseEntity.ok(photoService.getPhotoListByTag(photoTagRequestDto));
    }

    @PostMapping("/tag/{photoId}")
    public ResponseEntity<BasicResponse> getPhotoListByTagAndId(@PathVariable("photoId")Long photoId,
                                                                @RequestBody PhotoTagRequestDto photoTagRequestDto)
    {
        return ResponseEntity.ok(photoService.getPhotoListByTagAndId(photoId, photoTagRequestDto));
    }

    @GetMapping("/{photoId}/comments")
    public ResponseEntity<BasicResponse> getPhotoComments(@PathVariable Long photoId) {
        return ResponseEntity.ok(photoService.getPhotoComments(photoId));
    }

}