package com.wechat.notice.service;

import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.config.WeChatConfigBuilder;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.service.impl.WeChatAppConfigServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * WeChatAppConfigService测试类
 * 验证配置服务可以独立运行
 * 
 * @author fyf
 */
public class WeChatAppConfigServiceTest {
    
    private WeChatAppConfigService appConfigService;
    private WeChatNoticeProperties properties;
    
    @Before
    public void setUp() {
        // 使用WeChatConfigBuilder创建测试配置
        WeChatNoticeProperties.AppConfig testApp = new WeChatNoticeProperties.AppConfig();
        testApp.setCorpId("test-corp-id");
        testApp.setSecret("test-secret");
        testApp.setAgentId(1000001);
        testApp.setToken("test-token");
        testApp.setAesKey("test-aes-key-1234567890123456789012345678901234567890123");
        
        properties = WeChatConfigBuilder.create()
                .defaultApp("test-app")
                .addApp("test-app", testApp)
                .build();
        
        // 手动创建服务实例，无需Spring
        appConfigService = new WeChatAppConfigServiceImpl(properties);
    }
    
    @Test
    public void testGetDefaultApp() {
        WeChatAppConfig config = appConfigService.getDefaultApp();
        
        assertNotNull(config);
        assertEquals("test-app", config.getAppName());
        assertEquals("test-corp-id", config.getCorpId());
        assertEquals("test-secret", config.getSecret());
        assertEquals(Integer.valueOf(1000001), config.getAgentId());
        assertEquals("test-token", config.getToken());
        assertEquals("test-aes-key-1234567890123456789012345678901234567890123", config.getAesKey());
    }
    
    @Test
    public void testGetApp() {
        WeChatAppConfig config = appConfigService.getApp("test-app");
        
        assertNotNull(config);
        assertEquals("test-app", config.getAppName());
        assertEquals("test-corp-id", config.getCorpId());
    }
    
    @Test(expected = WeChatNoticeException.class)
    public void testGetAppNotFound() {
        appConfigService.getApp("non-exist-app");
    }
    
    @Test(expected = WeChatNoticeException.class)
    public void testGetAppWithNullName() {
        appConfigService.getApp(null);
    }
    
    @Test(expected = WeChatNoticeException.class)
    public void testGetAppWithEmptyName() {
        appConfigService.getApp("");
    }
    
    @Test
    public void testHasApp() {
        assertTrue(appConfigService.hasApp("test-app"));
        assertFalse(appConfigService.hasApp("non-exist-app"));
        assertFalse(appConfigService.hasApp(null));
        assertFalse(appConfigService.hasApp(""));
    }
    
    @Test
    public void testGetAllApps() {
        Map<String, WeChatAppConfig> allApps = appConfigService.getAllApps();

        assertNotNull(allApps);
        assertEquals(1, allApps.size());
        assertTrue(allApps.containsKey("test-app"));
    }
    
    @Test
    public void testMultipleApps() {
        // 创建多应用配置
        WeChatNoticeProperties.AppConfig app1 = new WeChatNoticeProperties.AppConfig();
        app1.setCorpId("corp1");
        app1.setSecret("secret1");
        app1.setAgentId(1000001);
        app1.setToken("token1");
        app1.setAesKey("aeskey1234567890123456789012345678901234567890123");
        
        WeChatNoticeProperties.AppConfig app2 = new WeChatNoticeProperties.AppConfig();
        app2.setCorpId("corp2");
        app2.setSecret("secret2");
        app2.setAgentId(1000002);
        app2.setToken("token2");
        app2.setAesKey("aeskey2234567890123456789012345678901234567890123");
        
        WeChatNoticeProperties multiProperties = WeChatConfigBuilder.create()
                .defaultApp("app1")
                .addApp("app1", app1)
                .addApp("app2", app2)
                .build();
        
        WeChatAppConfigService multiService = new WeChatAppConfigServiceImpl(multiProperties);
        
        // 测试获取默认应用
        WeChatAppConfig defaultApp = multiService.getDefaultApp();
        assertEquals("app1", defaultApp.getAppName());
        
        // 测试获取所有应用
        Map<String, WeChatAppConfig> allApps = multiService.getAllApps();
        assertEquals(2, allApps.size());
        
        // 测试检查应用存在
        assertTrue(multiService.hasApp("app1"));
        assertTrue(multiService.hasApp("app2"));
        assertFalse(multiService.hasApp("app3"));
    }
}