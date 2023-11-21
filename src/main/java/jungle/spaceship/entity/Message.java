package jungle.spaceship.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    @Enumerated
    private MessageType messageType;

    @Column
    private String content;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime createAt;


    public Message(MessageType messageType, String content, Long memberId, ChatRoom chatRoom) {
        this.messageType = messageType;
        this.content = content;
        this.memberId = memberId;
        this.chatRoom = chatRoom;
    }
}
