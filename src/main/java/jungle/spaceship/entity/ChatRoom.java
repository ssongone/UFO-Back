package jungle.spaceship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;


    private String createAt;

    public ChatRoom (String createAt){
        this.createAt = createAt;
    }

}
