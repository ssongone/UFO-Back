package jungle.spaceship.photo.controller;

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
    public String createPreSignedUrl(@RequestBody S3RegisterDto s3RegisterDto) {
        return s3Service.getPreSignedUrl(s3RegisterDto);

    }

    @PostMapping
    public ResponseEntity<BasicResponse> registerPhoto(@RequestBody PhotoRegisterDto photoDto){
        return ResponseEntity.ok(photoService.registerPhoto(photoDto));
    }
}
