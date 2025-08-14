package com.wechat.notice.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信消息发送结果
 * 
 * @author fyf
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatMessageResult {
    
    /**
     * 是否发送成功
     */
    private boolean success;
    
    /**
     * 消息ID
     */
    private String msgId;
    
    /**
     * 错误码
     */
    private Integer errCode;
    
    /**
     * 错误信息
     */
    private String errMsg;
    
    /**
     * 响应时间戳
     */
    private long timestamp;
    
    /**
     * 创建成功结果
     */
    public static WeChatMessageResult success(String msgId) {
        return WeChatMessageResult.builder()
            .success(true)
            .msgId(msgId)
            .timestamp(System.currentTimeMillis())
            .build();
    }
    
    /**
     * 创建失败结果
     */
    public static WeChatMessageResult failure(Integer errCode, String errMsg) {
        return WeChatMessageResult.builder()
            .success(false)
            .errCode(errCode)
            .errMsg(errMsg)
            .timestamp(System.currentTimeMillis())
            .build();
    }
}