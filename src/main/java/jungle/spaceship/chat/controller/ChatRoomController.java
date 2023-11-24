package jungle.spaceship.chat.controller;

import jungle.spaceship.chat.entity.ChatRoom;
import jungle.spaceship.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 생성
     */
    @PostMapping
    public ChatRoom createRoom(@RequestParam String roomName) {
        return chatRoomService.createRoom(roomName);
    }

    /**
     * 특정 채팅방 조회
     */
    @GetMapping("/{roomId}")
    public ChatRoom roomInfo(@PathVariable Long roomId){
        return chatRoomService.findRoomById(roomId);
    }

    /**
     * 모든 채팅방 반환
     */
    @GetMapping("/list")
    public List<ChatRoom> findAllRoom(){
        return chatRoomService.findAllRoom();
    }

}
