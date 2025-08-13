package com.wechat.notice.service.impl;

import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.service.WeChatAppConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信应用配置服务实现
 * 
 * @author WeChat Notice
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatAppConfigServiceImpl implements WeChatAppConfigService {
    
    private final WeChatNoticeProperties properties;
    private final Map<String, WeChatAppConfig> configCache = new ConcurrentHashMap<>();
    
    @Override
    public WeChatAppConfig getDefaultApp() {
        String defaultAppName = properties.getDefaultApp();
        if (StringUtils.isBlank(defaultAppName)) {
            throw new WeChatNoticeException("默认应用名称未配置");
        }
        return getApp(defaultAppName);
    }
    
    @Override
    public WeChatAppConfig getApp(String appName) {
        if (StringUtils.isBlank(appName)) {
            throw new WeChatNoticeException("应用名称不能为空");
        }
        
        // 先从缓存获取
        WeChatAppConfig cachedConfig = configCache.get(appName);
        if (cachedConfig != null) {
            return cachedConfig;
        }
        
        // 从配置中获取
        WeChatNoticeProperties.AppConfig appConfig = properties.getApps().get(appName);
        if (appConfig == null) {
            throw new WeChatNoticeException(String.format("未找到应用配置: %s", appName));
        }
        
        // 验证必要参数
        validateAppConfig(appName, appConfig);
        
        // 构建配置对象
        WeChatAppConfig weChatAppConfig = WeChatAppConfig.builder()
            .appName(appName)
            .corpId(appConfig.getCorpId())
            .secret(appConfig.getSecret())
            .agentId(appConfig.getAgentId())
            .token(appConfig.getToken())
            .aesKey(appConfig.getAesKey())
            .build();
        
        // 缓存配置
        configCache.put(appName, weChatAppConfig);
        
        log.debug("加载应用配置: appName={}, corpId={}, agentId={}", 
            appName, appConfig.getCorpId(), appConfig.getAgentId());
        
        return weChatAppConfig;
    }
    
    @Override
    public Map<String, WeChatAppConfig> getAllApps() {
        Map<String, WeChatAppConfig> allConfigs = new ConcurrentHashMap<>();
        
        for (String appName : properties.getApps().keySet()) {
            try {
                allConfigs.put(appName, getApp(appName));
            } catch (Exception e) {
                log.warn("获取应用配置失败: appName={}, error={}", appName, e.getMessage());
            }
        }
        
        return allConfigs;
    }
    
    @Override
    public boolean hasApp(String appName) {
        if (StringUtils.isBlank(appName)) {
            return false;
        }
        return properties.getApps().containsKey(appName);
    }
    
    /**
     * 验证应用配置
     */
    private void validateAppConfig(String appName, WeChatNoticeProperties.AppConfig appConfig) {
        if (StringUtils.isBlank(appConfig.getCorpId())) {
            throw new WeChatNoticeException(String.format("应用[%s]的corpId不能为空", appName));
        }
        if (StringUtils.isBlank(appConfig.getSecret())) {
            throw new WeChatNoticeException(String.format("应用[%s]的secret不能为空", appName));
        }
        if (appConfig.getAgentId() == null) {
            throw new WeChatNoticeException(String.format("应用[%s]的agentId不能为空", appName));
        }
        if (StringUtils.isBlank(appConfig.getToken())) {
            throw new WeChatNoticeException(String.format("应用[%s]的token不能为空", appName));
        }
        if (StringUtils.isBlank(appConfig.getAesKey())) {
            throw new WeChatNoticeException(String.format("应用[%s]的aesKey不能为空", appName));
        }
    }
}