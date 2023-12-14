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
    }),
    TINGLING((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", sender + "님이 찌릿통신을 보냈어요");
        map.put("message", "앱에 들어와서 응답해주세요!");
        return map;
    }),
    COMMENT((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Comment");
        map.put("message", sender +"님이 사진에 댓글을 남겼습니다");
        return map;
    }),
    GAME_RESULT((sender, message)-> {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "GAME");
        map.put("message", sender + "님의 " + message +" 결과가 발표되었습니다");
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
