package jungle.spaceship.member.controller.dto;

import com.google.firebase.messaging.Notification;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.notification.PushAlarm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class FamilyRegistrationDto implements PushAlarm {
    TokenInfo tokenInfo;
    String code;
    Member member;
    FamilyResponseDto familyResponseDto;

    @Override
    public Notification toNotification() {
        String newMemberName = this.member.getNickname();
        return Notification.builder()
                .setTitle("찌릿찌릿 " + newMemberName + " 등장")
                .setBody(NEW_FAMILY_ALARM_BODY).build();
    }

    @Override
    public Map<String, String> getAdditionalData() {
        Map<String, String> additionalData = new HashMap<>();

        additionalData.put("memberId", member.getMemberId().toString());
        additionalData.put("nickName", member.getName());
        additionalData.put("picture", member.getPicture());
        additionalData.put("familyRole", member.getFamilyRole().getRoleName());

        return additionalData;
    }

}
