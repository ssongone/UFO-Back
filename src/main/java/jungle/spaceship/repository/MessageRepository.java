package jungle.spaceship.repository;

import jungle.spaceship.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "select * from (select * from message where chat_room_Id = :roomId order by message_id desc limit :num) n order by n.message_id", nativeQuery = true)
    List<Message> findNumberOfMessageInChatRoomReverse(@Param("roomId") Long roomId, @Param("num") int num);

}
