package jungle.spaceship.member.service;

import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.repository.FamilyRepository;
import jungle.spaceship.member.repository.MemberRepository;
import jungle.spaceship.member.repository.PlantRepository;
import jungle.spaceship.notification.FcmService;
import jungle.spaceship.notification.NotificationType;
import jungle.spaceship.response.BasicResponse;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static jungle.spaceship.member.entity.Plant.ATTENDANCE_POINT;
import static jungle.spaceship.member.entity.Plant.TODAY_MISSION_POINT;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final FcmService fcmService;

    public Plant getPlant() {
        Long familyId = securityUtil.extractFamilyId();
        Family family = familyRepository.findById(familyId).orElseThrow(() -> new NoSuchElementException("가족 정보가 잘못됐어요"));
        return family.getPlant();
    }

//    @Transactional
//    public PlantStateDto performActivity(Long plantId, int pointsEarned) {
//        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new NoSuchElementException("해당하는 새싹이가 없어요"));
//        PlantStateDto plantStateDto = plant.performActivity(pointsEarned);
//        plantRepository.save(plant);
//        return plantStateDto;
//    }

    @Transactional
    public void performActivity(Member member, int pointsEarned) {
        int xp = member.updatePoint(pointsEarned);
        memberRepository.save(member);

        Plant plant = member.getFamily().getPlant();
        PlantStateDto plantStateDto = plant.performActivity(xp);
        plantRepository.save(plant);
        if (plantStateDto.isUp())
            fcmService.sendFcmMessageToFamilyExcludingMe(member, NotificationType.PLANT, String.valueOf(plantStateDto.getLevel()));
    }

    public PlantStateDto earnAttendancePoint(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new NoSuchElementException("Family not found with id: " + familyId));
        Plant plant = family.getPlant();
        PlantStateDto plantStateDto = plant.performActivity(ATTENDANCE_POINT);

        plantRepository.save(plant);

        return plantStateDto;
    }

    @Transactional
    public BasicResponse clearMission() {
        Member member = securityUtil.extractMember();
        member.updatePoint(TODAY_MISSION_POINT);
        memberRepository.save(member);

        Plant plant = member.getFamily().getPlant();
        PlantStateDto plantStateDto = plant.performActivity(TODAY_MISSION_POINT);
        if (plantStateDto.isUp())
            fcmService.sendFcmMessageToFamilyExcludingMe(member, NotificationType.PLANT, String.valueOf(plantStateDto.getLevel()));

        plantRepository.save(plant);
        return new ExtendedResponse<>(plantStateDto, HttpStatus.OK.value(), "오늘의 미션 성공! 새싹이가 자랐습니다");
    }
}
