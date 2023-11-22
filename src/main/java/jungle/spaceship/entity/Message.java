package jungle.spaceship.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String sender;

    private Long roomId;

    private String createAt;


    @Builder
    public Message(MessageType messageType, String content, String sender, Long roomId, String createAt) {
        this.messageType = messageType;
        this.content = content;
        this.sender = sender;
        this.roomId = roomId;
        this.createAt = createAt;
    }
}
