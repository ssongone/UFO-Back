package jungle.spaceship.photo.service;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.photo.controller.dto.S3RegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {ContextInstanceDataAutoConfiguration.class})
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final SecurityUtil securityUtil;


/*
1.버튼 클릭시

2.백엔드 서버에 presigendURL 호출

3.백엔드 서버는 토큰을 검사하고 유효한 토큰이라면~ presignedURL S3서버로부터 요청해서 다시 클라쪽에 설정한 유효기간동안 유효한 presigendURL을 넘겨줌

4.클라이언트는 presigendURL을 성공적으로 받았으면 두가지 요청을 수행

    4-1)발급받은 presigned URL을 통해 S3서버로 이미지 업로드

    4-2) 게시글 제목과 내용을 백엔드 서버로 전송 저장

5.백엔드 서버는 제목과 내용을 받고 저장경로를 받아와서 같이 저장해준다
 */

    /**
     * S3RegisterDto를 기반으로 파일 업로드용(PUT) 프리사인드 URL을 생성하여 반환
     * @return String pre-signed Url
     */
    public String getPreSignedUrl(S3RegisterDto s3RegisterDto){
        securityUtil.extractMember();
        String prefix = s3RegisterDto.getPrefix(); // 파일명에 추가될 프리픽스(prefix) 가져오기
        String fileName = s3RegisterDto.getFileName(); // 파일명 가져오기

        // 만약 프리픽스(prefix)가 비어있지 않다면 파일명에 추가
        if (!prefix.isEmpty()) {
            fileName = prefix + "/" + fileName;
        }

        // URL 생성을 위한 요청 객체 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(prefix, fileName);

        // Amazon S3 서비스를 통해 URL 생성
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
// https://your-s3-bucket.s3.amazonaws.com/photos/family/2023/2a1c0a3e-09f5-4d6d-9a27-94f5b42f3d95pic1.jpg?AWSAccessKeyId=your-access-key&Expires=1679795200&Signature=your-signature&x-amz-security-token=your-security-token
        return url.toString();
    }


    /**
     * 파일 업로드용(PUT) 프리사인드 URL 생성 요청 객체를 생성
     * @param prefix
     * @param fileName
     * @return
     */
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String prefix, String fileName) {

//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                new GeneratePresignedUrlRequest(bucket, fileName)
//                        .withMethod(HttpMethod.PUT)
//                        .withExpiration(getPreSignedUrlExpiration())
//                        .addRequestParameter("userToken", userToken);

        // 프리사인드 URL 생성 요청 객체 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, createPath(prefix, fileName))
                        .withMethod( HttpMethod.PUT )                   // 클라이언트는 HTTP PUT 메서드를 사용하여 업로드할 것이므로 PUT으로 설정
                        .withExpiration( getPreSignedUrlExpiration() ); // URL의 유효기간 설정

        // 예시 : https://your-bucket-name.s3.amazonaws.com/your-object-key?AWSAccessKeyId=your-access-key-id&Signature=your-signature&Expires=expiration-time
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());     // 업로드된 파일에 대한 ACL(Access Control List) 설정 (PublicRead로 설정)

        // 생성한 프리사인드 URL 생성 요청 객체 반환
        return generatePresignedUrlRequest;
    }

    /**
     * 프리사인드 URL의 유효 기간을 설정
     * @return 유효기간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2; // 현재 시간에서 2분 더한 시간으로 설정 (2분 동안 유효)
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    /**
     * 파일의 고유 ID를 생성
     * @return UUID (36자리)
     */
    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 파일의 전체 경로를 생성
     * @param prefix 디렉토리 경로
     * @param fileName 파일명
     * @return 파일의 전체 경로 photos/family/2023/2a1c0a3e-09f5-4d6d-9a27-94f5b42f3d95가족_사진.jpg
     */
    private String createPath(String prefix, String fileName) {
        String fileId = createFileId(); // 파일 고유 ID 생성
        return String.format("%s/%s", prefix, fileId + fileName); // 전체 경로 생성
    }

}