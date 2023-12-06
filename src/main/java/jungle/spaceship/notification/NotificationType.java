package jungle.spaceship.notification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum NotificationType {

    ADD_FAMILY_MEMBER((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Family");
        map.put("message", "눌러서 확인해보세요");
        return map;
    }),
    TMI((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "TMI");
        map.put("message", sender + ": " + message);
        return map;
    }),
    CHAT((sender, message) -> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", sender);
        map.put("message", message);
        return map;
    }),
    CALENDAR((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Calendar");
        map.put("message", "눌러서 확인해보세요");
        return map;
    }),
    PHOTO((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Photo");
        map.put("message", "눌러서 확인해보세요");
        return map;
    }),
    PLANT((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Plant");
        map.put("message", "눌러서 확인해보세요");
        return map;
    });


    private final BiFunction<String, String, Map<String, Object>> mapper;

    NotificationType(BiFunction<String, String, Map<String, Object>> mapper) {
        this.mapper = mapper;
    }

    public Map<String, Object> apply(String sender, String message) {
        return mapper.apply(sender, message);
    }


}
