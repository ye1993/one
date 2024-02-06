package cn.madcoder.one.framework.idempotent.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 幂等 Redis DAO
 *
 * @author madcoder
 */
@AllArgsConstructor
public class IdempotentRedisDAO {

    /**
     * 幂等操作
     *
     * KEY 格式：idempotent:%s // 参数为 uuid
     * VALUE 格式：String
     * 过期时间：不固定
     */
    private static final String IDEMPOTENT = "idempotent:%s";

    private final StringRedisTemplate redisTemplate;

    public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(key);
        return redisTemplate.opsForValue().setIfAbsent(redisKey, "", timeout, timeUnit);
    }


    public Boolean delete(String key){
        String redisKey = formatKey(key);
        if (!redisTemplate.hasKey(redisKey)||redisTemplate.delete(redisKey)){
            return true;
        }
        return redisTemplate.delete(redisKey);

    }
    private static String formatKey(String key) {
        return String.format(IDEMPOTENT, key);
    }

}
