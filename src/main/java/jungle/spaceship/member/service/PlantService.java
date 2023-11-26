package jungle.spaceship.member.service;

import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;

    @Transactional
    public PlantStateDto performActivity(Long plantId, int pointsEarned) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new NoSuchElementException("해당하는 새싹이가 없어요"));
        PlantStateDto plantStateDto = plant.performActivity(pointsEarned);
        plantRepository.save(plant);
        return plantStateDto;
    }

}
