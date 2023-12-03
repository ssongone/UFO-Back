package jungle.spaceship.member.service;


import jungle.spaceship.chat.entity.ChatRoom;
import jungle.spaceship.chat.repository.ChatRoomRepository;
import jungle.spaceship.jwt.JwtTokenProvider;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.jwt.TokenInfo;
import jungle.spaceship.member.controller.dto.*;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.entity.alien.Alien;
import jungle.spaceship.member.entity.alien.AlienType;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.member.entity.family.InvitationCode;
import jungle.spaceship.member.entity.family.Role;
import jungle.spaceship.member.entity.oauth.KakaoInfoResponse;
import jungle.spaceship.member.entity.oauth.OAuthInfoResponse;
import jungle.spaceship.member.repository.*;
import jungle.spaceship.notification.service.NotificationService;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static jungle.spaceship.member.entity.family.InvitationCode.CODE_CHARACTERS;
import static jungle.spaceship.member.entity.family.InvitationCode.CODE_LENGTH;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AlienRepository alienRepository;
    private final FamilyRepository familyRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final InvitationCodeRepository invitationCodeRepository;
    private final PlantRepository plantRepository;
    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUtil securityUtil;
    private final NotificationService notificationService;

    static String DEFAULT_TOKEN = "cZONPdOLQYCg3gxyiC736r:APA91bGYhF7Em9guyGqFxjDun9dbkanX0K0x2Gc3y13lF1TTcjrhvbXzvOldg11K5rQ_1wJkH1qfQV941-SbBLIjym4Nct75_zBB_UiaUaLsgWcf2Xo9eVrdtC9eYIlQy0RDgc8qodA0";

    static String OAUTH2_URL_KAKAO = "https://kapi.kakao.com/v2/user/me";

    public Optional<LoginResponseDto> loginWithKakao(String accessToken) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfo(accessToken);

        Optional<Member> memberByEmail = memberRepository.findByEmail(oAuthInfoResponse.getEmail());

        if (memberByEmail.isEmpty()) {
            memberRepository.save(new Member(oAuthInfoResponse));
            return Optional.empty();
        }

        Member member = memberByEmail.get();
        Family family = member.getFamily();
        System.out.println("family = " + family);
        System.out.println("member = " + member.getFamilyRole());
        if (family == null || member.getFamilyRole() == null) {
            return Optional.empty();
        }


        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        LoginResponseDto loginResponseDto = new LoginResponseDto(tokenInfo, member, familyResponseDto);
        return Optional.of(loginResponseDto);
    }

    public OAuthInfoResponse requestOAuthInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(OAUTH2_URL_KAKAO, request, KakaoInfoResponse.class);
    }

    public LoginResponseDto loginWithToken() {
        Member member = securityUtil.extractMember();
        Family family = member.getFamily();

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        return new LoginResponseDto(tokenInfo, member, familyResponseDto);
    }

    @Transactional
    public Optional<LoginResponseDto> signUpCurrentFamily(SignUpDto dto) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new RuntimeException("가족 코드가 잘못됐어요!"));

        Optional<Member> byEmail = memberRepository.findByEmail(dto.getEmail());
        if (byEmail.isEmpty()) {
            return Optional.empty();
        }

        Member member = byEmail.get();
        member.update(dto);
        Alien alien = new Alien(dto.getAlienType());
        Family family = invitationCode.getFamily();

        member.setFamily(family);
        member.setAlien(alien);
        family.getMembers().add(member);

        alienRepository.save(alien);
        memberRepository.save(member);
        familyRepository.save(family);

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        System.out.println(new LoginResponseDto(tokenInfo, member, familyResponseDto));
        return Optional.of(new LoginResponseDto(tokenInfo, member, familyResponseDto));
    }

    @Transactional
    public Optional<LoginResponseDto> signUpNewFamily(SignUpDto dto) {
        Optional<Member> byEmail = memberRepository.findByEmail(dto.getEmail());
        if (byEmail.isEmpty()) {
            return Optional.empty();
        }
        Member member = byEmail.get();
        member.update(dto);
        Alien alien = new Alien(dto.getAlienType());
        Plant plant = new Plant(dto.getPlantName());
        ChatRoom chatRoom = new ChatRoom();
        Family family = new Family();
        family.setUfoName(dto.getUfoName());
        family.setPlant(plant);
        family.setChatRoom(chatRoom);
        family.getMembers().add(member);
        member.setFamily(family);
        member.setAlien(alien);
        plantRepository.save(plant);
        chatRoomRepository.save(chatRoom);
        alienRepository.save(alien);
        memberRepository.save(member);
        familyRepository.save(family);


        InvitationCode invitationCode = new InvitationCode(dto.getCode(), family);
        invitationCodeRepository.save(invitationCode);

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        System.out.println(new LoginResponseDto(tokenInfo, member, familyResponseDto));
        return Optional.of(new LoginResponseDto(tokenInfo, member, familyResponseDto));
    }


    public void signUp(SignUpDto dto) {
        Member member = securityUtil.extractMember();
        member.update(dto);
        memberRepository.save(member);
    }


    public void registerAlien(AlienDto dto) {
        Member member = securityUtil.extractMember();
        Alien alien = new Alien(dto);
        member.setAlien(alienRepository.save(alien));
        memberRepository.save(member);
    }

    @Transactional
    public ExtendedResponse<FamilyRegistrationDto> registerFamily(FamilyDto dto) {
        String code = dto.getCode();
        String firebaseToken = dto.getFirebaseToken();
        if (firebaseToken == null || firebaseToken.isEmpty())
            firebaseToken = DEFAULT_TOKEN;
        System.out.println("firebaseToken = " + firebaseToken);
        Member member = securityUtil.extractMember();
        ChatRoom chatRoom = new ChatRoom(dto.getCreatedAt());
        Plant plant = new Plant(dto.getPlantName());
        Family family = new Family(dto, chatRoom,plant);

        family.getMembers().add(member);
        family.setPlant(plant);
        member.setFamily(family);
        member.setRole(Role.USER);
        member.setFirebaseToken(firebaseToken);

        plantRepository.save(plant);
        chatRoomRepository.save(chatRoom);
        memberRepository.save(member);
        familyRepository.save(family);

        InvitationCode invitationCode = new InvitationCode(code, family);
        invitationCodeRepository.save(invitationCode);

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(),family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        FamilyRegistrationDto familyRegistrationDto = new FamilyRegistrationDto(tokenInfo, code, member, familyResponseDto);
        System.out.println("familyRegistrationDto = " + familyRegistrationDto);
        return new ExtendedResponse<>(familyRegistrationDto, HttpStatus.CREATED.value(), "가족이 생성되었습니다");

    }

    @Transactional
    public ExtendedResponse<FamilyRegistrationDto> registerCurrentFamily(FamilyDto dto) {
        String code = dto.getCode();
        String firebaseToken = dto.getFirebaseToken();
        if (firebaseToken.isEmpty())
            firebaseToken = DEFAULT_TOKEN;
        System.out.println("firebaseToken = " + firebaseToken);
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("코드가 유효하지 않습니다"));

        Family family = invitationCode.getFamily();
        Member member = securityUtil.extractMember();
        member.setRole(Role.USER);
        member.setFamily(family);
        member.setFirebaseToken(firebaseToken);
        family.getMembers().add(member);

        memberRepository.save(member);
        familyRepository.save(family);

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenByMember(member.getEmail(), member.getRole().getKey(), family.getFamilyId());
        FamilyResponseDto familyResponseDto = new FamilyResponseDto(family);
        FamilyRegistrationDto familyRegistrationDto = new FamilyRegistrationDto(tokenInfo, code, member, familyResponseDto);

        notificationService.sendMessageToFamilyExcludingMe(familyRegistrationDto, member);
        return new ExtendedResponse<>(familyRegistrationDto, HttpStatus.OK.value(), "가족에 등록되었습니다");
    }

    public String makeCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CODE_CHARACTERS.length());
            char randomChar = CODE_CHARACTERS.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        String code = codeBuilder.toString();
        if (invitationCodeRepository.findByCode(code).isPresent()) {
            code = makeCode();
        }
        return code;
    }

    public boolean validateCode(String code) {
        return invitationCodeRepository.findByCode(code).isPresent();
    }

    public FamilyInfoResponseDto requestFamilyInfo(String familyCode) {
        FamilyInfoResponseDto response = new FamilyInfoResponseDto();
        InvitationCode invitationCode = invitationCodeRepository.findByCode(familyCode)
                .orElseThrow(() -> new NoSuchElementException("코드가 유효하지 않습니다"));
        Family family = invitationCode.getFamily();
        List<Member> members = family.getMembers();
        if (members.isEmpty())
            return response;

        for (Member member : members) {
            FamilyRole nowRole = member.getFamilyRole();
            AlienType nowType = member.getAlien().getType();
            response.getRoles().get(nowRole.ordinal()).setEnabled(false);
            response.getTypes().get(nowType.ordinal()).setEnabled(false);
        }
        return response;
    }

//    public Member updateCharacter(CharacterDto characterDto) {
//        Member member = securityUtil.extractMember();
//        member.updateCharacter(characterDto);
//
//        memberRepository.save(member);
//        Family family = member.getFamily();
//        System.out.println("family.getMembers() = " + family.getMembers());
//
//        notificationService.sendMessageToFamilyExcludingMe(member, member);
//        return member;
//    }
//
//    public Family updateFamily(FamilyDto dto) {
//        Member member = securityUtil.extractMember();
//        Long familyId = securityUtil.extractFamilyId();
//        Family family = familyRepository.findById(familyId)
//                .orElseThrow(() -> new NoSuchElementException("Family not found with id: " + familyId));
//
//        family.getPlant().setName(dto.getPlantName());
//        family.setUfoName(dto.getUfoName());
//
//        familyRepository.save(family);
//        notificationService.sendMessageToFamilyExcludingMe(dto, member);
//        return family;
//
//    }
}
