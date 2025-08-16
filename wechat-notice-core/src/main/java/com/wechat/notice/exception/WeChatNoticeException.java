package com.wechat.notice.exception;

import lombok.Getter;

/**
 * 微信通知自定义异常
 * 
 * @author fyf
 */
@Getter
public class WeChatNoticeException extends RuntimeException {
    
    /**
     * 错误码
     */
    private Integer errorCode;
    
    /**
     * 构造方法
     *
     * @param message 错误信息
     */
    public WeChatNoticeException(String message) {
        super(message);
    }
    
    /**
     * 构造方法
     *
     * @param message 错误信息
     * @param cause 引起异常的原因
     */
    public WeChatNoticeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public WeChatNoticeException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param message 错误信息
     * @param cause 引起异常的原因
     */
    public WeChatNoticeException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}