package jungle.spaceship.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    @Value("${fcm.endpoint}")
    private String FCM_ENDPOINT;

    @Value("${fcm.key}")
    private String SERVER_KEY;

//    public void sendFcmMessage(String deviceToken, String title, String message, String additionalDataKey, String additionalDataValue) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "key=" + LEGACY_SERVER_KEY);
//
//        String requestBody = buildFcmRequestBody(deviceToken, title, message, additionalDataKey, additionalDataValue);
//        System.out.println("FCM Request Body: " + requestBody);
//
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(FCM_ENDPOINT, HttpMethod.POST, entity, String.class);
//
//        // Handle the response as needed
//        System.out.println("FCM Response: " + response.getBody());
//    }



}
