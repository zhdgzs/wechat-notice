package com.wechat.notice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信应用配置实体
 * 
 * @author fyf
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeChatAppConfig {
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * 企业ID
     */
    private String corpId;
    
    /**
     * 应用Secret
     */
    private String secret;
    
    /**
     * 应用ID
     */
    private Integer agentId;
    
    /**
     * Token
     */
    private String token;
    
    /**
     * AES Key
     */
    private String aesKey;
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 令牌过期时间
     */
    private Long tokenExpiresTime;
}