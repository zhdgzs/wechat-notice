package com.wechat.notice.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * WeChatConfigBuilder测试类
 * 验证core模块可以完全独立于Spring运行
 * 
 * @author fyf
 */
public class WeChatConfigBuilderTest {
    
    @Test
    public void testBuildDefaultConfig() {
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .enabled(true)
                .defaultApp("test-app")
                .build();
        
        assertNotNull(properties);
        assertTrue(properties.isEnabled());
        assertEquals("test-app", properties.getDefaultApp());
        assertNotNull(properties.getApi());
        assertNotNull(properties.getPortal());
        assertNotNull(properties.getApps());
    }
    
    @Test
    public void testBuildWithAppConfig() {
        WeChatNoticeProperties.AppConfig appConfig = new WeChatNoticeProperties.AppConfig();
        appConfig.setCorpId("test-corp-id");
        appConfig.setSecret("test-secret");
        appConfig.setAgentId(1000001);
        appConfig.setToken("test-token");
        appConfig.setAesKey("test-aes-key");
        
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .defaultApp("my-app")
                .addApp("my-app", appConfig)
                .build();
        
        assertEquals("my-app", properties.getDefaultApp());
        assertTrue(properties.getApps().containsKey("my-app"));
        
        WeChatNoticeProperties.AppConfig savedConfig = properties.getApps().get("my-app");
        assertEquals("test-corp-id", savedConfig.getCorpId());
        assertEquals("test-secret", savedConfig.getSecret());
        assertEquals(Integer.valueOf(1000001), savedConfig.getAgentId());
        assertEquals("test-token", savedConfig.getToken());
        assertEquals("test-aes-key", savedConfig.getAesKey());
    }
    
    @Test
    public void testBuildWithCustomApi() {
        WeChatNoticeProperties.Api api = new WeChatNoticeProperties.Api();
        api.setBaseUrl("https://custom.api.com");
        api.setConnectTimeout(5000);
        api.setReadTimeout(15000);
        api.setRetryEnabled(false);
        api.setRetryCount(1);
        
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .api(api)
                .build();
        
        WeChatNoticeProperties.Api savedApi = properties.getApi();
        assertEquals("https://custom.api.com", savedApi.getBaseUrl());
        assertEquals(5000, savedApi.getConnectTimeout());
        assertEquals(15000, savedApi.getReadTimeout());
        assertFalse(savedApi.isRetryEnabled());
        assertEquals(1, savedApi.getRetryCount());
    }
    
    @Test
    public void testBuildWithCustomPortal() {
        WeChatNoticeProperties.Portal portal = new WeChatNoticeProperties.Portal();
        portal.setEnabled(false);
        portal.setPath("/custom/portal");
        
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .portal(portal)
                .build();
        
        WeChatNoticeProperties.Portal savedPortal = properties.getPortal();
        assertFalse(savedPortal.isEnabled());
        assertEquals("/custom/portal", savedPortal.getPath());
    }
    
    @Test
    public void testChainedConfiguration() {
        WeChatNoticeProperties.AppConfig app1 = new WeChatNoticeProperties.AppConfig();
        app1.setCorpId("corp1");
        app1.setSecret("secret1");
        app1.setAgentId(1000001);
        app1.setToken("token1");
        app1.setAesKey("key1");
        
        WeChatNoticeProperties.AppConfig app2 = new WeChatNoticeProperties.AppConfig();
        app2.setCorpId("corp2");
        app2.setSecret("secret2");
        app2.setAgentId(1000002);
        app2.setToken("token2");
        app2.setAesKey("key2");
        
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .enabled(true)
                .defaultApp("app1")
                .addApp("app1", app1)
                .addApp("app2", app2)
                .build();
        
        assertTrue(properties.isEnabled());
        assertEquals("app1", properties.getDefaultApp());
        assertEquals(2, properties.getApps().size());
        assertTrue(properties.getApps().containsKey("app1"));
        assertTrue(properties.getApps().containsKey("app2"));
    }
}