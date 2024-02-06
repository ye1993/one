package cn.madcoder.one.framework.idempotent.config;


import cn.madcoder.one.framework.idempotent.core.aop.IdempotentAspect;
import cn.madcoder.one.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.madcoder.one.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import cn.madcoder.one.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.madcoder.one.framework.idempotent.core.redis.IdempotentRedisDAO;
import cn.madcoder.one.framework.redis.config.OneRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = OneRedisAutoConfiguration.class)
public class OneIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
