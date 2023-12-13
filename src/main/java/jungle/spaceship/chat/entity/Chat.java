package jungle.spaceship.chat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chat_Id;

    @Enumerated
    private ChatType chatType;

    @Column
    private String content;

    private String sender;

    private Long roomId;

    private String createAt;


    @Builder
    public Chat(ChatType chatType, String content, String sender, Long roomId, String createAt) {
        this.chatType = chatType;
        this.content = content;
        this.sender = sender;
        this.roomId = roomId;
        this.createAt = createAt;
    }
}
