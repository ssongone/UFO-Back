package jungle.spaceship.member.service;

import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.entity.family.Family;
import jungle.spaceship.member.repository.FamilyRepository;
import jungle.spaceship.member.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static jungle.spaceship.member.entity.Plant.ATTENDANCE_POINT;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public PlantStateDto performActivity(Long plantId, int pointsEarned) {

        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new NoSuchElementException("해당하는 새싹이가 없어요"));
        PlantStateDto plantStateDto = plant.performActivity(pointsEarned);
        plantRepository.save(plant);
        return plantStateDto;
    }

    public PlantStateDto earnAttendancePoint(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new NoSuchElementException("Family not found with id: " + familyId));
        Plant plant = family.getPlant();
        PlantStateDto plantStateDto = plant.performActivity(ATTENDANCE_POINT);

        plantRepository.save(plant);

        return plantStateDto;
    }

}
