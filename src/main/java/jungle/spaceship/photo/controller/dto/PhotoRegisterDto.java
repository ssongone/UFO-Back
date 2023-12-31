package jungle.spaceship.photo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.entity.Photo;

import java.util.List;

public record PhotoRegisterDto(
        @JsonProperty("photoTags") List<String> photoTags,
        @JsonProperty("description") String description,
        @JsonProperty("photoKey") String photoKey,
        @JsonProperty("writer") String writer)

        {


    public Photo toPhoto(Member member) {
        return new Photo(description, photoKey, member);
    }



}
