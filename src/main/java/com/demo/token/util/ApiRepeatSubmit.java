package com.demo.token.util;

import com.demo.token.common.ConstantUtils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @功能描述 防止重复提交标记注解
 */
@Target(ElementType.METHOD) // 作用到方法上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface ApiRepeatSubmit {
    ConstantUtils value();
} 