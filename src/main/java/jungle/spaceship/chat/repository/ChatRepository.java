package jungle.spaceship.chat.repository;

import jungle.spaceship.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "select * from (select * from chat where room_id = :roomId order by chat_id desc limit :num) n order by n.chat_id", nativeQuery = true)
    List<Chat> findNumberOfMessageInChatRoomReverse(@Param("roomId") Long roomId, @Param("num") int num);

}
