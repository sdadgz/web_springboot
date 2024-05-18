package cn.sdadgz.web_springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis配置
 *
 * <p>
 * 废物本物
 * </p>
 *
 * @author sdadgz
 * @since 2024/3/21 17:01:40
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 连接，这逼东西得放第一位
        template.setConnectionFactory(redisConnectionFactory);

        // hash 用 string
        template.setHashKeySerializer(RedisSerializer.string());
        // key
        template.setKeySerializer(RedisSerializer.string());

        // 默认用json
//        template.setDefaultSerializer(getRedisJsonSerializer());
        template.setDefaultSerializer(RedisSerializer.json());

        // 赋默认值
        template.afterPropertiesSet();
        return template;
    }

    // 废弃了，之前用自带的跑不了是因为没把连接放首位，用默认的就挺好的，我是废物
//    // redis序列化用的json
//    private static Jackson2JsonRedisSerializer<Object> getRedisJsonSerializer() {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        jackson2JsonRedisSerializer.setObjectMapper(getObjectMapper());
//        return jackson2JsonRedisSerializer;
//    }
//
//    // ObjectMapper
//    private static ObjectMapper getObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 妈的不清楚ObjectMapper是个什么东西，他妈的能跑就完事了，反正我是废物
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//        return objectMapper;
//    }

}
