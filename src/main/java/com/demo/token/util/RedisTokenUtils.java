package com.demo.token.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
    public boolean findToken(String tokenKey){
        String token = (String) redisTemplate.opsForValue().get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        // token 获取成功后 删除对应tokenMapstoken
        redisTemplate.delete(tokenKey);
        return true;
    }


} 