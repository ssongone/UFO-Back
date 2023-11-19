package jungle.spaceship.config;

import jungle.spaceship.repository.RedisMessageCache;
import jungle.spaceship.service.MessageService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private final RedisMessageCache redisMessageCache;
    private final MessageService messageService;
    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     * @param redisMessageCache
     * @param messageService
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, RedisMessageCache redisMessageCache, MessageService messageService) {
        super(listenerContainer);
        this.redisMessageCache = redisMessageCache;
        this.messageService = messageService;
    }


    //만료이벤트가 발생하면 발동하는 함수
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //2번 캐시에서 roomId를 뽑아냄
        Long roomId = Long.valueOf(message.toString().substring(4));
        LinkedList<jungle.spaceship.entity.Message> messages = redisMessageCache.get(roomId);
        Queue<jungle.spaceship.entity.Message> messageQueue = new LinkedList<>(messages);
        //roomId로 1번캐시의 Value를 알아내 DB에 commit
        messageService.commitMessageQueue(messageQueue);
        //남아있는 1번캐시를 삭제
        redisMessageCache.deleteKey(roomId);
    }
}
