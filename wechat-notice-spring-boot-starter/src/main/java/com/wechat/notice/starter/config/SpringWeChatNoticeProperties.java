package com.wechat.notice.starter.config;

import com.wechat.notice.config.WeChatNoticeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot配置属性适配器
 * 
 * @author fyf
 */
@Data
@ConfigurationProperties(prefix = "wechat.notice")
public class SpringWeChatNoticeProperties {
    
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
    
    /**
     * 转换为Core模块的配置对象
     */
    public WeChatNoticeProperties toCoreProperties() {
        WeChatNoticeProperties coreProperties = new WeChatNoticeProperties();
        coreProperties.setEnabled(this.enabled);
        coreProperties.setDefaultApp(this.defaultApp);
        
        // 转换Portal配置
        WeChatNoticeProperties.Portal corePortal = new WeChatNoticeProperties.Portal();
        corePortal.setEnabled(this.portal.enabled);
        corePortal.setPath(this.portal.path);
        coreProperties.setPortal(corePortal);
        
        // 转换API配置
        WeChatNoticeProperties.Api coreApi = new WeChatNoticeProperties.Api();
        coreApi.setBaseUrl(this.api.baseUrl);
        coreApi.setConnectTimeout(this.api.connectTimeout);
        coreApi.setReadTimeout(this.api.readTimeout);
        coreApi.setRetryEnabled(this.api.retryEnabled);
        coreApi.setRetryCount(this.api.retryCount);
        coreProperties.setApi(coreApi);
        
        // 转换应用配置
        Map<String, WeChatNoticeProperties.AppConfig> coreApps = getStringAppConfigMap();
        coreProperties.setApps(coreApps);
        
        return coreProperties;
    }

    private Map<String, WeChatNoticeProperties.AppConfig> getStringAppConfigMap() {
        Map<String, WeChatNoticeProperties.AppConfig> coreApps = new HashMap<>();
        for (Map.Entry<String, AppConfig> entry : this.apps.entrySet()) {
            AppConfig springApp = entry.getValue();
            WeChatNoticeProperties.AppConfig coreApp = new WeChatNoticeProperties.AppConfig();
            coreApp.setCorpId(springApp.corpId);
            coreApp.setSecret(springApp.secret);
            coreApp.setAgentId(springApp.agentId);
            coreApp.setToken(springApp.token);
            coreApp.setAesKey(springApp.aesKey);
            coreApps.put(entry.getKey(), coreApp);
        }
        return coreApps;
    }
}