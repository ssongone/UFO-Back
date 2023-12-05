package jungle.spaceship.photo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jungle.spaceship.member.entity.family.FamilyRole;

public record PhotoTagRequestDto(
        @JsonProperty("photoTag") FamilyRole familyRole) {
}
