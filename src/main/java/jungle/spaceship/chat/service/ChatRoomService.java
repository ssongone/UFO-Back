package jungle.spaceship.chat.service;

import jungle.spaceship.chat.entity.ChatRoom;
import jungle.spaceship.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 전체 채팅방 목록 조회
     */
    public List<ChatRoom> findAllRoom(){
        return chatRoomRepository.findAll();
    }

    /**
     * 채팅방 찾기
     */
    public ChatRoom findRoomById(Long roomId){
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    /**
     * 채팅방 생성
     */
    public ChatRoom createRoom(String roomName){
        // 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(roomName);
        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

}
