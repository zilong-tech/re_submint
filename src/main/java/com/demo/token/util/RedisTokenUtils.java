package com.demo.token.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisTokenUtils {

    private long timeout = 2;//过期时间

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取Token 并将Token保存至redis
     * @return
     */
    public String getToken() {
        String token = "token_"+ UUID.randomUUID();
        redisTemplate.opsForValue().set(token,token,timeout, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 判断Token是否存在 并且删除Token
     * @param tokenKey
     * @return
     */
    public boolean validToken(String tokenKey){
        // 设置 Lua 脚本，其中 KEYS[1] 是 key，KEYS[2] 是 value
        String script = "if redis.call('get', KEYS[1]) == KEYS[2] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        // 执行 Lua 脚本
        Long result = (Long) redisTemplate.execute(redisScript, Arrays.asList(tokenKey, tokenKey));
        // 根据返回结果判断是否成功成功匹配并删除 Redis 键值对，若果结果不为空和0，则验证通过
        if (result != null && result != 0L) {
            redisTemplate.delete(tokenKey);

            return true;
        }
        return false;
    }


} 