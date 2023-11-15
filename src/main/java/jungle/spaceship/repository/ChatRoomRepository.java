package jungle.spaceship.repository;

import jungle.spaceship.dto.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRooms;

    /**
     * ChatService 빈 생성 후 자동으로 호출되는
     * ChatRoom 초기화 작업
     */
    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
        /*
         * LinkedHashMap은 이중연결 리스트로 HashMap과 달리 삽입한 순서대로 출력된다.
         * HashMap 와 비교했을 때, LinkedHashMap 은 Map create 시간은 더 걸리지만, Access와 Iterate 속도가 조금 더 빠르다.
         */
    }

    /**
     * 전체 채팅방 목록 조회
     */
    public List<ChatRoom> findAllRoom(){
        List<ChatRoom> chatRoomList = new ArrayList<>(chatRooms.values());
        Collections.reverse(chatRoomList);
        return chatRoomList;
    }

    /**
     * 채팅방 찾기
     */
    public ChatRoom findRoomById(String roomId){
        return chatRooms.get(roomId);
    }

    /**
     * 채팅방 생성 + UUID로 채팅방 ID 설정
     */
    public ChatRoom createRoom(String name){
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

}
