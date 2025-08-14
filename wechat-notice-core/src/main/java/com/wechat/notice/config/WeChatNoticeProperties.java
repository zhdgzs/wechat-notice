package com.wechat.notice.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信通知配置属性
 * 
 * @author fyf
 */
@Data
public class WeChatNoticeProperties {
    
    /**
     * 是否启用微信通知功能
     */
    private boolean enabled = true;
    
    /**
     * 默认应用名称
     */
    private String defaultApp = "default";
    
    /**
     * Portal相关配置
     */
    private Portal portal = new Portal();
    
    /**
     * 应用配置映射
     */
    private Map<String, AppConfig> apps = new HashMap<>();
    
    /**
     * API相关配置
     */
    private Api api = new Api();
    
    /**
     * Portal配置
     */
    @Data
    public static class Portal {
        /**
         * 是否启用Portal控制器
         */
        private boolean enabled = true;
        
        /**
         * Portal接口路径
         */
        private String path = "/wx/cp/portal";
    }
    
    /**
     * 单个应用配置
     */
    @Data
    public static class AppConfig {
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
         * Token（用于接口验证）
         */
        private String token;
        
        /**
         * AES Key（用于消息加解密）
         */
        private String aesKey;
    }
    
    /**
     * API配置
     */
    @Data
    public static class Api {
        /**
         * 企业微信API基础URL，默认为官方地址
         * 可配置为代理地址以解决网络访问问题
         */
        private String baseUrl = "https://qyapi.weixin.qq.com";
        
        /**
         * 连接超时时间（毫秒）
         */
        private int connectTimeout = 10000;
        
        /**
         * 读取超时时间（毫秒）
         */
        private int readTimeout = 30000;
        
        /**
         * 是否启用重试机制
         */
        private boolean retryEnabled = true;
        
        /**
         * 重试次数
         */
        private int retryCount = 3;
    }
}