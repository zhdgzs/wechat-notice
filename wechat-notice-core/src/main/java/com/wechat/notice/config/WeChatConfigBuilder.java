package com.wechat.notice.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信通知配置构建器
 * 
 * @author fyf
 */
public class WeChatConfigBuilder {
    
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
    private WeChatNoticeProperties.Portal portal = new WeChatNoticeProperties.Portal();
    
    /**
     * 应用配置映射
     */
    private final Map<String, WeChatNoticeProperties.AppConfig> apps = new HashMap<>();
    
    /**
     * API相关配置
     */
    private WeChatNoticeProperties.Api api = new WeChatNoticeProperties.Api();
    
    /**
     * 创建配置构建器实例
     *
     * @return 配置构建器实例
     */
    public static WeChatConfigBuilder create() {
        return new WeChatConfigBuilder();
    }
    
    /**
     * 设置是否启用微信通知功能
     *
     * @param enabled 是否启用
     * @return 构建器实例
     */
    public WeChatConfigBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    /**
     * 设置默认应用名称
     *
     * @param defaultApp 默认应用名称
     * @return 构建器实例
     */
    public WeChatConfigBuilder defaultApp(String defaultApp) {
        this.defaultApp = defaultApp;
        return this;
    }
    
    /**
     * 设置Portal配置
     *
     * @param portal Portal配置
     * @return 构建器实例
     */
    public WeChatConfigBuilder portal(WeChatNoticeProperties.Portal portal) {
        this.portal = portal;
        return this;
    }
    
    /**
     * 添加应用配置
     *
     * @param name 应用名称
     * @param appConfig 应用配置
     * @return 构建器实例
     */
    public WeChatConfigBuilder addApp(String name, WeChatNoticeProperties.AppConfig appConfig) {
        this.apps.put(name, appConfig);
        return this;
    }
    
    /**
     * 设置API配置
     *
     * @param api API配置
     * @return 构建器实例
     */
    public WeChatConfigBuilder api(WeChatNoticeProperties.Api api) {
        this.api = api;
        return this;
    }
    
    /**
     * 构建配置对象
     *
     * @return 微信通知配置属性对象
     */
    public WeChatNoticeProperties build() {
        WeChatNoticeProperties properties = new WeChatNoticeProperties();
        properties.setEnabled(enabled);
        properties.setDefaultApp(defaultApp);
        properties.setPortal(portal);
        properties.setApps(apps);
        properties.setApi(api);
        return properties;
    }
}