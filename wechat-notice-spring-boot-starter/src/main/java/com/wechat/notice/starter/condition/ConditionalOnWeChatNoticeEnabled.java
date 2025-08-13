package com.wechat.notice.starter.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * 微信通知功能启用条件注解
 * 
 * @author WeChat Notice
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(
    prefix = "wechat.notice", 
    name = "enabled", 
    havingValue = "true", 
    matchIfMissing = true
)
public @interface ConditionalOnWeChatNoticeEnabled {
}