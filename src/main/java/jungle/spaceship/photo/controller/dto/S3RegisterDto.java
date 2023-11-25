package jungle.spaceship.photo.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3RegisterDto {

    private final String fileName;

    private final String prefix;
}
