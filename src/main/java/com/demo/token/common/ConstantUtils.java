package com.demo.token.common;

/**
 * 【定义从哪里取Token的枚举类】
 * head 即从请求头中取token，即客户端将token放入请求头来请求后端数据
 * body 即直接从请求体中取token
 */
public enum ConstantUtils {
    BOOD,HEAD
} 