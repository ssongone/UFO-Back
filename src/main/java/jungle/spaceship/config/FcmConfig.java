package jungle.spaceship.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FcmConfig {

    // FirebaseMessaging 빈을 생성하는 메서드
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        // FirebaseApp 인스턴스를 가져옴
        FirebaseApp firebaseApp = getFirebaseApp();

        // FirebaseMessaging 인스턴스를 반환
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    // FirebaseApp 인스턴스를 가져오는 메서드
    private FirebaseApp getFirebaseApp() throws IOException {
        // Firebase 서비스 계정 (비공개)키 파일을 가져옴
        ClassPathResource resource = new ClassPathResource("firebase/ufo-back-firebase-adminsdk-bkil7-e09d8abb38.json");
        InputStream token = resource.getInputStream();

        // FirebaseApp 인스턴스를 가져옴
        FirebaseApp firebaseApp = FirebaseApp.getApps().isEmpty() ?
                FirebaseApp.initializeApp(getFirebaseOptions(token)) :
                FirebaseApp.getInstance();

        return firebaseApp;
    }

    // FirebaseOptions를 설정하는 메서드
    private FirebaseOptions getFirebaseOptions(InputStream token) throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(token))
                .build();
    }
}

