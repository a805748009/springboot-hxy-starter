package com.hxy.springboot.starter.auth.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hxy.springboot.starter.auth.SecurityConfig;
import com.hxy.springboot.starter.model.TimeEntity;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.SetObjectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnBean(RedissonClient.class)
public class AuthUserCache<V> {
    private final Logger logger = LoggerFactory.getLogger(AuthUserCache.class);
    private final RedissonClient redissonClient;
    private final SecurityConfig securityConfig;

    private volatile Cache<String, TimeEntity<V>> CACHE =
            Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).expireAfterAccess(1, TimeUnit.HOURS).maximumSize(5000).build();

    public AuthUserCache(RedissonClient redissonClient, SecurityConfig securityConfig) {
        this.redissonClient = redissonClient;
        this.securityConfig = securityConfig;
    }

    public V getLoginUser(String accessToken) {
        TimeEntity<V> timeEntity = CACHE.getIfPresent(accessToken);
        if (timeEntity == null || timeEntity.getTimeOut() < System.currentTimeMillis()) {
            RBucket<V> bucket = redissonClient.getBucket(accessToken);
            V v = bucket.get();
            if (v != null) {
                long ttl = bucket.remainTimeToLive();
                autoRefresh(accessToken, ttl);
                CACHE.put(accessToken, new TimeEntity<>(v, System.currentTimeMillis() + ttl));
            }
            return v;
        } else {
            autoRefresh(accessToken, timeEntity.getTimeOut() - System.currentTimeMillis());
            return timeEntity.getT();
        }
    }

    public void setLoginUser(String accessToken, V v) {
        CACHE.put(accessToken, new TimeEntity<>(v, System.currentTimeMillis() + securityConfig.getSessionTimeOut()));
        RBucket<V> bucket = redissonClient.getBucket(accessToken);
        bucket.set(v, securityConfig.getSessionTimeOut(), TimeUnit.MILLISECONDS);
        bucket.addListener((DeletedObjectListener) s -> {
            logger.debug("DeletedObjectListener ,accessToken:{} ", s);
            CACHE.invalidate(s);
        });
        bucket.addListener((ExpiredObjectListener) s -> {
            logger.debug("ExpiredObjectListener ,accessToken:{} ", s);
            CACHE.invalidate(s);
        });
        bucket.addListener((SetObjectListener) s -> {
            logger.debug("SetObjectListener ,accessToken:{} ", s);
            CACHE.invalidate(s);
        });
        logger.debug("setLoginUser success,accessToken:{} ", accessToken);
    }

    public void logout(String accessToken) {
        CACHE.invalidate(accessToken);
        redissonClient.<V>getBucket(accessToken).deleteAsync();
        logger.debug("logout success,accessToken:{}", accessToken);
    }

    public boolean isLogin(String accessToken) {
        return getLoginUser(accessToken) != null;
    }

    public void autoRefresh(String accessToken, Long remainTimeToLive) {
        if (securityConfig.isAutoRefresh()) {
            if (remainTimeToLive < securityConfig.getSessionTimeOut() / 5) {
                addTime(accessToken);
            }
        }
    }

    public void addTime(String accessToken) {
        CACHE.invalidate(accessToken);
        redissonClient.<V>getBucket(accessToken).expire(securityConfig.getSessionTimeOut(), TimeUnit.MILLISECONDS);
    }
}
