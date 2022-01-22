package com.demo.aop;


import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis 方案
 *
 * @author Levin
 * @since 2018/6/12 0012
 */
@Aspect
@Configuration
public class LockMethodInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static String DATA = "data";

    @Around("execution(public * *(..)) && @annotation(com.demo.aop.Resubmit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Resubmit lock = method.getAnnotation(Resubmit.class);
        Object[] pointArgs = pjp.getArgs();

        String lockKey =  DigestUtils.md5Hex(getRequest(pointArgs));

        String value = UUID.randomUUID().toString();
        try {

            Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, value);
            redisTemplate.expire(lockKey,lock.delaySeconds(), TimeUnit.SECONDS);
            if (!success) {
                throw new RuntimeException("重复提交");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        } finally {
           redisTemplate.delete(lockKey);
        }
    }

    private String getRequest(Object... params) {
        if (params == null) {
            return "[]";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Object param : params) {
                if (param instanceof HttpServletRequest
                        || param instanceof HttpServletResponse
                        || param instanceof BindResult
                        || param instanceof ModelMap
                        || param instanceof Model
                        || param instanceof byte[]) {
                    continue;
                }

                sb.append(JSON.toJSON(param));

                sb.append(",");
            }
            if (sb.lastIndexOf(",") != -1) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return "error happen while print log";
        }
    }


}