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

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    private String createAt;

    @Builder
    public Message(MessageType messageType, String content, Long memberId, ChatRoom chatRoom, String createAt) {
        this.messageType = messageType;
        this.content = content;
        this.memberId = memberId;
        this.chatRoom = chatRoom;
        this.createAt = createAt;
    }
}
