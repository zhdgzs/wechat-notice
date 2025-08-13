package com.wechat.notice.exception;

/**
 * 微信通知自定义异常
 * 
 * @author WeChat Notice
 */
public class WeChatNoticeException extends RuntimeException {
    
    private Integer errorCode;
    
    public WeChatNoticeException(String message) {
        super(message);
    }
    
    public WeChatNoticeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WeChatNoticeException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public WeChatNoticeException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public Integer getErrorCode() {
        return errorCode;
    }
}