package jungle.spaceship.chat.repository;

import jungle.spaceship.chat.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisMessageCache {
    @Autowired
    @Qualifier("messageRedisTemplate")
    private final RedisTemplate<String, LinkedList<Chat>> redisTemplate;

    @Value("${spring.redis.expire}")
    private int expireTime;

    public void put(Long roomId, Queue<Chat> chatQueue){
        redisTemplate.opsForValue().set(roomId.toString(), new LinkedList<>(chatQueue));
        redisTemplate.expire(roomId.toString(), expireTime, TimeUnit.MINUTES);
    }

    public boolean containsKey(Long roomId){
        return Boolean.TRUE.equals(redisTemplate.hasKey(roomId.toString()));
    }
    public LinkedList<Chat> get(Long roomId){

        return redisTemplate.opsForValue().get(roomId.toString());
    }

    public Queue<Chat> values(){
        return get(Long.valueOf(Objects.requireNonNull(redisTemplate.randomKey())));
    }

    public void deleteKey(Long roomId){
        redisTemplate.delete(roomId.toString());
    }
    public void deleteAll(){
        redisTemplate.discard();
    }
}