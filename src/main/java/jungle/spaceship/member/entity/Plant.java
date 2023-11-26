package jungle.spaceship.member.entity;

import jungle.spaceship.member.controller.dto.PlantStateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long plantId;
    private int level;
    private int point;
    private String name;

    public static final int[] LEVEL_THRESHOLD = {10, 20, 40, 80, 120, 160, 200, 250, 300, 350, 400};

    public PlantStateDto performActivity(int pointsEarned) {
        this.point += pointsEarned;
        System.out.println("pointsEarned = " + pointsEarned);
        return new PlantStateDto(this.name, point, checkLevelUp());
    }

    private boolean checkLevelUp() {
        boolean isUp = false;
        while (LEVEL_THRESHOLD[level] >= point) {
            level++;
            isUp = true;
        }
        System.out.println("level = " + level);
        return isUp;
    }

    public Plant(String name) {
        this.level = 0;
        this.point = 0;
        this.name = name;
    }
}
