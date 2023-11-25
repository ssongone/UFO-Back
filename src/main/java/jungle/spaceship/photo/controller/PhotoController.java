package jungle.spaceship.photo.controller;

import jungle.spaceship.photo.controller.dto.PhotoRegisterDto;
import jungle.spaceship.photo.service.PhotoService;
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

    @PostMapping
    public ResponseEntity<BasicResponse> registerPhoto(@RequestBody PhotoRegisterDto photoDto){
        return ResponseEntity.ok(photoService.registerPhoto(photoDto));
    }
}
