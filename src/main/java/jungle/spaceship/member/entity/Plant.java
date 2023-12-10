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

    public static final int[] LEVEL_THRESHOLD = {20, 50, 100, 160, 250, 300, 400, 600, 900, 1500, 2000};
    public static final int TODAY_MISSION_POINT = 3;
    public static final int ATTENDANCE_POINT = 1;
    public static final int TMI_POINT = 1;
    public static final int UPLOAD_PHOTO_POINT = 2;
    public static final int ADD_COMMENT_POINT = 1;
    public static final int ADD_EVENT_POINT = 1;

    public PlantStateDto performActivity(int pointsEarned) {
        this.point += pointsEarned;
        System.out.println("pointsEarned = " + pointsEarned);
        boolean result = checkLevelUp();
        return new PlantStateDto(this.name, point, level, result);
    }

    private boolean checkLevelUp() {
        boolean isUp = false;
        while (level < LEVEL_THRESHOLD.length) {
            if (point < LEVEL_THRESHOLD[level])
                break;
            level++;
            isUp = true;
        }
        return isUp;
    }

    public Plant(String name) {
        this.level = 0;
        this.point = 0;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
