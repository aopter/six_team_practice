package com.sixpoints.config;

import com.google.common.hash.Funnels;
import com.sixpoints.utils.BloomFilterHelper;
import com.sixpoints.utils.RedisBloomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

//    @Bean //redisTemplate注入到Spring容器
//    public RedisTemplate<Object,Object> redisTemplateOther(RedisConnectionFactory factory){
//        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        redisTemplate.setConnectionFactory(factory);
//
//        redisTemplate.setDefaultSerializer(redisSerializer);
//
//        return redisTemplate;
//    }
//
//    @Bean //redisTemplate注入到Spring容器
//    public RedisTemplate<String,Object> myRedisTemplate(RedisConnectionFactory factory){
//        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        redisTemplate.setConnectionFactory(factory);
//
//        redisTemplate.setDefaultSerializer(redisSerializer);
//
//        return redisTemplate;
//    }

//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() {
//        RedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
//                Object.class);
//        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair
//                .fromSerializer(jackson2JsonRedisSerializer);
//
//        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(serializationPair);
//    }
//
//    @Bean
//    @Primary
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration());
//        RedisCacheManager cm = builder.build();
//        return cm;
//    }

    //userId的布隆过滤器
    @Bean
    @Scope("singleton")
    public BloomFilterHelper userIdBloomFilterHelper(){
        return new BloomFilterHelper<>(Funnels.integerFunnel(), 100000, 0.1);
    }

    //dynastyId的布隆过滤器
    @Bean
    @Scope("singleton")
    public BloomFilterHelper dynastyIdBloomFilterHelper(){
        return new BloomFilterHelper<>(Funnels.integerFunnel(), 50, 0.1);
    }

    //incidentId的布隆过滤器
    @Bean
    @Scope("singleton")
    public BloomFilterHelper incidentIdBloomFilterHelper(){
        return new BloomFilterHelper<>(Funnels.integerFunnel(), 10000, 0.1);
    }

    //cardId的布隆过滤器
    @Bean
    @Scope("singleton")
    public BloomFilterHelper cardIdBloomFilterHelper(){
        return new BloomFilterHelper<>(Funnels.integerFunnel(), 10000, 0.1);
    }

    //cardId的布隆过滤器
    @Bean
    @Scope("singleton")
    public BloomFilterHelper problemIdBloomFilterHelper(){
        return new BloomFilterHelper<>(Funnels.integerFunnel(), 100000, 0.1);
    }
}
