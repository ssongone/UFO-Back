package jungle.spaceship.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.family.Family;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FcmService {

    @Value("${fcm.endpoint}")
    private String FCM_ENDPOINT;

    @Value("${fcm.key}")
    private String SERVER_KEY;

    public void sendFcmMessageToFamilyExcludingMe(Member member, NotificationType type, String content) {
        Family family = member.getFamily();
        if (family.getMembers().size() < 2) {
            return;
        }

        List<String> tokens =  family.getMembers().stream()
                .filter(m -> !m.equals(member))
                .map(Member::getFirebaseToken)
                .collect(Collectors.toList());

        tokens.stream().forEach(token-> sendFcmMessage(token, type, member.getNickname(), content));
    }

    private void sendFcmMessage(String firebaseToken, NotificationType type, String sender, String content) {
        if (firebaseToken == null) {
            System.out.println("토큰없뜸");
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + SERVER_KEY);

        String requestBody = null;
        try {
            requestBody = makeResponse(firebaseToken, type, sender, content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(FCM_ENDPOINT, HttpMethod.POST, entity, String.class);
    }

    private String makeResponse(String firebaseToken, NotificationType type, String sender, String content) throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("to", firebaseToken);

        Map<String, Object> dataMap = type.apply(sender, content);
        jsonMap.put("data", dataMap);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jsonMap);
        return result;
    }


    public void tingle(Member from, Member to) {
        String firebaseToken = to.getFirebaseToken();
        sendFcmMessage(firebaseToken, NotificationType.TINGLING, from.getNickname(), "");
    }

}
