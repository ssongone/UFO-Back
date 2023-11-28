package jungle.spaceship.photo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;



public record S3RegisterDto(
        @JsonProperty("fileName") String fileName,
        @JsonProperty("prefix") String prefix) {

}
