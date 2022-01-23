package com.demo.token.aop;

import javax.servlet.http.HttpServletRequest;

import com.demo.token.common.ConstantUtils;
import com.demo.token.util.ApiToken;
import com.demo.token.util.ApiRepeatSubmit;
import com.demo.token.util.RedisTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @功能描述 aop解析注解
 */
@Aspect
@Component
public class NoRepeatSubmitAop {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private RedisTokenUtils redisTokenUtils;

    /**
     * 将token放入请求
     * @param pjp
     * @param nrs
     */
    @Before("execution(* com.demo.token.controller.*Controller.*(..)) && @annotation(nrs)")
    public void before(JoinPoint pjp, ApiToken nrs){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.setAttribute("token", redisTokenUtils.getToken());
    }


    /**
     * 拦截带有重复请求的注解的方法
     * @param pjp
     * @param nrs
     * @return
     */
    @Around("execution(* com.demo.token.controller.*Controller.*(..)) && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, ApiRepeatSubmit nrs) {

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            String token = null;
            if (nrs.value() == ConstantUtils.BOOD){
                //从请求体中取Token
                token = (String) request.getAttribute("token");
            }else if (nrs.value() == ConstantUtils.HEAD){
                //从请求头中取Token
                token = request.getHeader("token");
            }
            if (StringUtils.isEmpty(token)){
                return "token 不存在";
            }
            if (!redisTokenUtils.validToken(token)){
                return "请勿重复提交";
            }
            Object o = pjp.proceed();
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("验证重复提交时出现未知异常!");
            return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
        }

    }

}