package jungle.spaceship.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FcmService {

    @Value("${fcm.endpoint}")
    private String FCM_ENDPOINT;

    @Value("${fcm.key}")
    private String SERVER_KEY;

    public void sendFcmMessage(String firebaseToken, NotificationType type, String sender, String content) {
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
        System.out.println("entity = " + entity);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(FCM_ENDPOINT, HttpMethod.POST, entity, String.class);

        System.out.println("FCM Response: " + response.getBody());
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

}
