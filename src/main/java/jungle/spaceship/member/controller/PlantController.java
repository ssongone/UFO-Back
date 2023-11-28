package jungle.spaceship.member.controller;

import jungle.spaceship.member.controller.dto.PlantStateDto;
import jungle.spaceship.member.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/plant")
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/point/{plantId}/{xp}")
    public PlantStateDto earnPoint(@PathVariable Long plantId, @PathVariable int xp) {
        return plantService.performActivity(plantId, xp);
    }



}
