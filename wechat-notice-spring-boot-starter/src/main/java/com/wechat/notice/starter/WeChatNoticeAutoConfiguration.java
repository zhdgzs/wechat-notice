package com.wechat.notice.starter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wechat.notice.client.WeChatApiClient;
import com.wechat.notice.client.WeChatTokenManager;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.starter.config.SpringWeChatNoticeProperties;
import com.wechat.notice.service.WeChatAppConfigService;
import com.wechat.notice.service.WeChatNoticeService;
import com.wechat.notice.service.impl.WeChatAppConfigServiceImpl;
import com.wechat.notice.service.impl.WeChatNoticeServiceImpl;
import com.wechat.notice.starter.condition.ConditionalOnWeChatNoticeEnabled;
import com.wechat.notice.starter.condition.ConditionalOnWeChatPortalEnabled;
import com.wechat.notice.starter.controller.WeChatPortalController;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 微信通知自动配置类
 * 
 * @author fyf
 */
@Slf4j
@Configuration
@ConditionalOnClass({WeChatNoticeService.class, ObjectMapper.class})
@ConditionalOnWeChatNoticeEnabled
@EnableConfigurationProperties(SpringWeChatNoticeProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WeChatNoticeAutoConfiguration {
    
    /**
     * ObjectMapper配置
     */
    @Bean("weChatObjectMapper")
    @ConditionalOnMissingBean(name = "weChatObjectMapper")
    public ObjectMapper weChatObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 反序列化配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        // 序列化配置
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        log.debug("WeChat ObjectMapper配置完成");
        return mapper;
    }

    /**
     * HTTP客户端配置
     */
    @Bean("weChatHttpClient")
    @ConditionalOnMissingBean(name = "weChatHttpClient")
    public CloseableHttpClient weChatHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(50)
                .evictExpiredConnections()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .build();
        
        log.debug("WeChat HttpClient配置完成");
        return httpClient;
    }
    
    /**
     * Core配置适配器
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatNoticeProperties weChatNoticeProperties(SpringWeChatNoticeProperties springProperties) {
        return springProperties.toCoreProperties();
    }
    
    /**
     * Token管理器配置
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatTokenManager weChatTokenManager(CloseableHttpClient weChatHttpClient, 
                                                ObjectMapper weChatObjectMapper,
                                                WeChatNoticeProperties properties) {
        log.debug("WeChat TokenManager配置完成");
        return new WeChatTokenManager(weChatHttpClient, weChatObjectMapper, properties);
    }
    
    /**
     * API客户端配置
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatApiClient weChatApiClient(CloseableHttpClient weChatHttpClient,
                                          ObjectMapper weChatObjectMapper,
                                          WeChatTokenManager weChatTokenManager,
                                          WeChatNoticeProperties properties) {
        log.debug("WeChat ApiClient配置完成");
        return new WeChatApiClient(weChatHttpClient, weChatObjectMapper, weChatTokenManager, properties);
    }
    
    /**
     * 应用配置服务配置
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatAppConfigService weChatAppConfigService(WeChatNoticeProperties properties) {
        log.debug("WeChat AppConfigService配置完成");
        return new WeChatAppConfigServiceImpl(properties);
    }
    
    /**
     * 通知服务配置
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatNoticeService weChatNoticeService(WeChatApiClient weChatApiClient,
                                                   WeChatAppConfigService weChatAppConfigService,
                                                   WeChatNoticeProperties properties) {
        log.debug("WeChat NoticeService配置完成");
        return new WeChatNoticeServiceImpl(weChatApiClient, weChatAppConfigService, properties);
    }
    
    /**
     * Portal控制器配置
     */
    @Bean
    @ConditionalOnWeChatPortalEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean
    public WeChatPortalController weChatPortalController(WeChatAppConfigService weChatAppConfigService) {
        log.debug("WeChat PortalController配置完成");
        return new WeChatPortalController(weChatAppConfigService);
    }
    
    @PostConstruct
    public void init() {
        log.info("WeChat Notice 自动配置已启用");
    }
}