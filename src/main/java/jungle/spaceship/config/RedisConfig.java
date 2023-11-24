package jungle.spaceship.config;


import jungle.spaceship.chat.entity.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.LinkedList;

@EnableCaching
@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean(name = "messageRedisTemplate")
    public RedisTemplate<String, LinkedList<Chat>> messageRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate<String, LinkedList<Chat>> messageRedisTemplate = new RedisTemplate<>();
        messageRedisTemplate.setConnectionFactory(redisConnectionFactory);
        messageRedisTemplate.setKeySerializer(new StringRedisSerializer());
        messageRedisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        return messageRedisTemplate;
    }

    /**
     * Redis 메시지를 수신하는 리스너 컨테이너
     * - Redis에 연결하고, 메시지를 수신하기 위해 등록된 리스너를 관리
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }




}