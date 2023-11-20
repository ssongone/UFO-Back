package jungle.spaceship.repository;

import jungle.spaceship.entity.Message;
import lombok.RequiredArgsConstructor;
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
    private final RedisTemplate<String, LinkedList<Message>> redisTemplate;

    @Value("${spring.redis.expire}")
    private int expireTime;

    public void put(Long roomId, Queue<Message> messageQueue){
        redisTemplate.opsForValue().set(roomId.toString(), new LinkedList<>(messageQueue));
        redisTemplate.expire(roomId.toString(), expireTime, TimeUnit.MINUTES);
    }

    public boolean containsKey(Long roomId){
        return Boolean.TRUE.equals(redisTemplate.hasKey(roomId.toString()));
    }
    public LinkedList<Message> get(Long roomId){

        return redisTemplate.opsForValue().get(roomId.toString());
    }

    public Queue<Message> values(){
        return get(Long.valueOf(Objects.requireNonNull(redisTemplate.randomKey())));
    }

    public void deleteKey(Long roomId){
        redisTemplate.delete(roomId.toString());
    }
    public void deleteAll(){
        redisTemplate.discard();
    }
}