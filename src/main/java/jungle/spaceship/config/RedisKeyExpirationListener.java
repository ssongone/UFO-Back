package jungle.spaceship.config;

import jungle.spaceship.chat.entity.Chat;
import jungle.spaceship.chat.repository.RedisMessageCache;
import jungle.spaceship.chat.service.ChatService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private final RedisMessageCache redisMessageCache;
    private final ChatService chatService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, RedisMessageCache redisMessageCache, ChatService chatService) {
        super(listenerContainer);
        this.redisMessageCache = redisMessageCache;
        this.chatService = chatService;
    }


    //만료이벤트가 발생하면 발동하는 함수
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //2번 캐시에서 roomId를 뽑아냄
        Long roomId = Long.valueOf(message.toString().substring(4));
        LinkedList<Chat> chats = redisMessageCache.get(roomId);
        Queue<Chat> chatQueue = new LinkedList<>(chats);
        //roomId로 1번캐시의 Value를 알아내 DB에 commit
        chatService.commitMessageQueue(chatQueue);
        //남아있는 1번캐시를 삭제
        redisMessageCache.deleteKey(roomId);
    }
}
