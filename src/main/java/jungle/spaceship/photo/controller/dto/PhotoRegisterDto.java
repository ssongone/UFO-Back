package jungle.spaceship.photo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.messaging.Notification;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.notification.PushAlarm;
import jungle.spaceship.photo.entity.Photo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public record PhotoRegisterDto(
        @JsonProperty("photoTags") List<FamilyRole> photoTags,
        @JsonProperty("description") String description,
        @JsonProperty("photoKey") String photoKey,
        @JsonProperty("writer") String writer)

        implements PushAlarm {


    public Photo toPhoto(Member member) {
        return new Photo(description, photoKey, member);
    }



    @Override
    public Notification toNotification() {

        Notification notification = Notification.builder()
                .setTitle("찌릿찌릿 " + writer + "님이 새로운 사진을 올렸어요")
                .setBody(description)
                .build();

        return notification;
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();
        additionalData.put("description", description);
        additionalData.put("writer", writer);

        return additionalData;
    }
}
