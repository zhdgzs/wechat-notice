package com.wechat.notice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.message.WeChatApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信Token管理器
 * 
 * @author fyf
 */
@Slf4j
@RequiredArgsConstructor
public class WeChatTokenManager {
    
    /**
     * HTTP客户端
     */
    private final CloseableHttpClient httpClient;
    
    /**
     * JSON序列化工具
     */
    private final ObjectMapper objectMapper;
    
    /**
     * 微信通知配置属性
     */
    private final WeChatNoticeProperties properties;
    
    /**
     * Token缓存，Key为corpId + "_" + agentId
     */
    private final Map<String, WeChatAppConfig> tokenCache = new ConcurrentHashMap<>();
    
    /**
     * 获取访问令牌（带缓存）
     *
     * @param appConfig 应用配置
     * @return 访问令牌
     */
    public String getAccessToken(WeChatAppConfig appConfig) {
        String cacheKey = appConfig.getCorpId() + "_" + appConfig.getAgentId();
        
        WeChatAppConfig cachedConfig = tokenCache.get(cacheKey);
        if (cachedConfig != null && isTokenValid(cachedConfig)) {
            log.debug("使用缓存Token: appName={}", appConfig.getAppName());
            return cachedConfig.getAccessToken();
        }
        
        // 获取新token
        String newToken = fetchAccessToken(appConfig);
        
        // 更新缓存
        WeChatAppConfig newConfig = appConfig.toBuilder()
            .accessToken(newToken)
            .tokenExpiresTime(System.currentTimeMillis() + 7000 * 1000) // 7000秒后过期
            .build();
        
        tokenCache.put(cacheKey, newConfig);
        
        log.info("获取新Token成功: appName={}", appConfig.getAppName());
        return newToken;
    }
    
    /**
     * 检查Token是否有效
     *
     * @param config 应用配置
     * @return true-有效，false-无效
     */
    private boolean isTokenValid(WeChatAppConfig config) {
        return config.getAccessToken() != null && 
               config.getTokenExpiresTime() != null &&
               config.getTokenExpiresTime() > System.currentTimeMillis();
    }
    
    /**
     * 从微信服务器获取Token
     *
     * @param appConfig 应用配置
     * @return 新的访问令牌
     * @throws WeChatNoticeException 获取Token失败时抛出
     */
    private String fetchAccessToken(WeChatAppConfig appConfig) {
        String baseUrl = properties.getApi().getBaseUrl();
        String url = String.format("%s/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
            baseUrl, appConfig.getCorpId(), appConfig.getSecret());
        
        log.debug("获取访问Token: corpId={}, agentId={}", appConfig.getCorpId(), appConfig.getAgentId());
        
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", "WeChat-Notice-Client/1.0.0");
        
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            WeChatApiResponse apiResponse = objectMapper.readValue(responseBody, WeChatApiResponse.class);
            
            if (apiResponse.getErrcode() != null && apiResponse.getErrcode() != 0) {
                throw new WeChatNoticeException(String.format("获取Token失败: %d - %s", 
                    apiResponse.getErrcode(), apiResponse.getErrmsg()));
            }
            
            if (apiResponse.getAccessToken() == null) {
                throw new WeChatNoticeException("获取Token响应异常，accessToken为空");
            }
            
            return apiResponse.getAccessToken();
            
        } catch (IOException e) {
            log.error("获取Token HTTP请求失败: corpId={}", appConfig.getCorpId(), e);
            throw new WeChatNoticeException("获取Token HTTP请求失败", e);
        }
    }
    
    /**
     * 清除Token缓存
     *
     * @param appConfig 应用配置
     */
    public void clearTokenCache(WeChatAppConfig appConfig) {
        String cacheKey = appConfig.getCorpId() + "_" + appConfig.getAgentId();
        tokenCache.remove(cacheKey);
        log.info("清除Token缓存: appName={}", appConfig.getAppName());
    }
}