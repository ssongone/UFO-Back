package jungle.spaceship.member.controller;

import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.member.entity.Plant;
import jungle.spaceship.member.service.PlantService;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/plant")
    public ExtendedResponse<Plant> getPlant() {
        return new ExtendedResponse<>(plantService.getPlant(), HttpStatus.OK.value(), "");
    }

    @GetMapping("/api/1/point/{plantId}/{xp}")
    public PlantStateDto earnPoint(@PathVariable Long plantId, @PathVariable int xp) {
        return plantService.performActivity(plantId, xp);
    }


}
