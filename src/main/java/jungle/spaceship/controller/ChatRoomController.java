package jungle.spaceship.controller;

import jungle.spaceship.dto.ChatRoom;
import jungle.spaceship.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 생성
     */
    @PostMapping
    public ChatRoom createRoom(@RequestParam String name){
        return chatRoomRepository.createRoom(name);
    }

    /**
     * 특정 채팅방 조회
     */
    @GetMapping("/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId){
        return chatRoomRepository.findRoomById(roomId);
    }

    /**
     * 모든 채팅방 반환
     */
    @GetMapping("/list")
    public List<ChatRoom> findAllRoom(){
        return chatRoomRepository.findAllRoom();
    }

}
