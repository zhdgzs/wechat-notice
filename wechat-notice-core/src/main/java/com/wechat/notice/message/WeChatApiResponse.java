package com.wechat.notice.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信API响应实体
 * 
 * @author WeChat Notice
 */
@Data
public class WeChatApiResponse {
    
    /**
     * 错误码
     */
    private Integer errcode;
    
    /**
     * 错误信息
     */
    private String errmsg;
    
    /**
     * 消息ID
     */
    private String msgid;
    
    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;
    
    /**
     * 令牌有效期（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;
}