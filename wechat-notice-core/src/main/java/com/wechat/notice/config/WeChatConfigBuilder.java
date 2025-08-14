package com.wechat.notice.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信通知配置构建器
 * 
 * @author fyf
 */
public class WeChatConfigBuilder {
    
    private boolean enabled = true;
    private String defaultApp = "default";
    private WeChatNoticeProperties.Portal portal = new WeChatNoticeProperties.Portal();
    private Map<String, WeChatNoticeProperties.AppConfig> apps = new HashMap<>();
    private WeChatNoticeProperties.Api api = new WeChatNoticeProperties.Api();
    
    public static WeChatConfigBuilder create() {
        return new WeChatConfigBuilder();
    }
    
    public WeChatConfigBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public WeChatConfigBuilder defaultApp(String defaultApp) {
        this.defaultApp = defaultApp;
        return this;
    }
    
    public WeChatConfigBuilder portal(WeChatNoticeProperties.Portal portal) {
        this.portal = portal;
        return this;
    }
    
    public WeChatConfigBuilder addApp(String name, WeChatNoticeProperties.AppConfig appConfig) {
        this.apps.put(name, appConfig);
        return this;
    }
    
    public WeChatConfigBuilder api(WeChatNoticeProperties.Api api) {
        this.api = api;
        return this;
    }
    
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