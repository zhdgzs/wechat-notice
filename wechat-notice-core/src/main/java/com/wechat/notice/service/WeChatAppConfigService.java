package com.wechat.notice.service;

import com.wechat.notice.config.WeChatAppConfig;

import java.util.Map;

/**
 * 微信应用配置服务接口
 * 
 * @author WeChat Notice
 */
public interface WeChatAppConfigService {
    
    /**
     * 获取默认应用配置
     *
     * @return 应用配置
     */
    WeChatAppConfig getDefaultApp();
    
    /**
     * 获取指定应用配置
     *
     * @param appName 应用名称
     * @return 应用配置
     */
    WeChatAppConfig getApp(String appName);
    
    /**
     * 获取所有应用配置
     *
     * @return 应用配置映射
     */
    Map<String, WeChatAppConfig> getAllApps();
    
    /**
     * 检查应用是否存在
     *
     * @param appName 应用名称
     * @return 是否存在
     */
    boolean hasApp(String appName);
}