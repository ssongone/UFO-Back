package jungle.spaceship.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class ChatRoom {

    private String roomId;

    private String name;


    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}
