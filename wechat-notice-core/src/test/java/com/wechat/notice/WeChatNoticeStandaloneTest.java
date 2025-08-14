package com.wechat.notice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.notice.client.WeChatApiClient;
import com.wechat.notice.client.WeChatTokenManager;
import com.wechat.notice.config.WeChatConfigBuilder;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.builder.WeChatMessageBuilder;
import com.wechat.notice.service.WeChatAppConfigService;
import com.wechat.notice.service.WeChatNoticeService;
import com.wechat.notice.service.impl.WeChatAppConfigServiceImpl;
import com.wechat.notice.service.impl.WeChatNoticeServiceImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 微信通知独立运行测试
 * 演示core模块完全不依赖Spring的使用方式
 * 
 * @author fyf
 */
public class WeChatNoticeStandaloneTest {
    
    private WeChatNoticeService weChatNoticeService;
    
    @Before
    public void setUp() {
        // 1. 创建配置
        WeChatNoticeProperties.AppConfig appConfig = new WeChatNoticeProperties.AppConfig();
        // 只配置这3个即可进行测试。需要提前通过企业微信应用的api接口验证
        appConfig.setCorpId("test-corp-id");
        appConfig.setSecret("test-secret");
        appConfig.setAgentId(1000001);

        appConfig.setToken("test-token");
        appConfig.setAesKey("test-aes-key-1234567890123456789012345678901234567890123");
        
        WeChatNoticeProperties properties = WeChatConfigBuilder.create()
                .enabled(true)
                .defaultApp("default")
                .addApp("default", appConfig)
                .build();
        
        // 2. 手动创建依赖对象（无Spring容器）
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 3. 按依赖顺序创建服务
        WeChatTokenManager tokenManager = new WeChatTokenManager(httpClient, objectMapper, properties);
        WeChatApiClient apiClient = new WeChatApiClient(httpClient, objectMapper, tokenManager, properties);
        WeChatAppConfigService appConfigService = new WeChatAppConfigServiceImpl(properties);
        
        // 4. 创建主服务
        weChatNoticeService = new WeChatNoticeServiceImpl(apiClient, appConfigService, properties);
    }

    @Test
    public void testSendTextMessage() {
        WeChatMessage message = WeChatMessageBuilder.text()
                .content("这是一条文本测试消息")
                .toUser("@all")
                .build();
        weChatNoticeService.sendMessage(message);
    }

    @Test
    public void testSendMarkdownMessage() {
        // 微信不支持查看markdown消息
        String markdown = "## 这是一条Markdown测试消息";

        WeChatMessage message = WeChatMessageBuilder.markdown()
                .content(markdown)
                .toUser("@all")
                .build();
        weChatNoticeService.sendMessage(message);
    }

    @Test
    public void testSendNewsMessage() {
        WeChatMessage message = WeChatMessageBuilder.news()
                .addArticle(
                        "这是一条图文测试消息",
                        "这是一条图文测试消息 描述",
                        "https://www.baidu.com/",
                        "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png"
                )
                .toUser("@all")
                .build();
        weChatNoticeService.sendMessage(message);
    }

    @Test
    public void testSendTextCardMessage() {
        WeChatMessage message = WeChatMessageBuilder.textCard()
                .title("这是一条TextCard测试消息")
                .description("这是一条TextCard测试消息 描述")
                .url("https://www.baidu.com/")
                .toUser("@all")
                .build();
        weChatNoticeService.sendMessage(message);
    }

    @Test
    public void testServiceInitialization() {
        assertNotNull(weChatNoticeService);
        System.out.println("✅ WeChatNoticeService 初始化成功，完全独立于Spring");
    }
    
    @Test
    public void testMessageBuilder() {
        WeChatMessage message = WeChatMessageBuilder.text()
                .content("这是一条测试消息")
                .toUser("test-user")
                .build();
        
        assertNotNull(message);
        assertEquals("text", message.getMsgType());
        assertEquals("这是一条测试消息", message.getContent());
        assertEquals("test-user", message.getToUser());
        
        System.out.println("✅ 消息构建器工作正常");
    }

    @Test
    public void testStandaloneUsageExample() {
        // 演示完整的独立使用流程
        System.out.println("=== 微信通知Core模块独立使用演示 ===");
        
        // 1. 配置创建
        WeChatNoticeProperties.AppConfig myApp = new WeChatNoticeProperties.AppConfig();
        myApp.setCorpId("my-corp-id");
        myApp.setSecret("my-secret");
        myApp.setAgentId(1000001);
        myApp.setToken("my-token");
        myApp.setAesKey("my-aes-key-1234567890123456789012345678901234567890123");
        
        WeChatNoticeProperties config = WeChatConfigBuilder.create()
                .enabled(true)
                .defaultApp("my-app")
                .addApp("my-app", myApp)
                .build();
        
        System.out.println("1. ✅ 配置创建完成");
        
        // 2. 手动组装服务（模拟非Spring环境）
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();
        WeChatTokenManager tokenManager = new WeChatTokenManager(httpClient, objectMapper, config);
        WeChatApiClient apiClient = new WeChatApiClient(httpClient, objectMapper, tokenManager, config);
        WeChatAppConfigService appConfigService = new WeChatAppConfigServiceImpl(config);
        WeChatNoticeService noticeService = new WeChatNoticeServiceImpl(apiClient, appConfigService, config);
        
        System.out.println("2. ✅ 服务组装完成");
        
        // 3. 构建消息
        WeChatMessage message = WeChatMessageBuilder.text()
                .content("Hello from standalone core module!")
                .toUser("@all")
                .build();
        
        System.out.println("3. ✅ 消息构建完成");
        
        // 4. 验证服务可用性（不发送真实请求）
        assertNotNull(noticeService);
        assertNotNull(message);
        assertEquals("Hello from standalone core module!", message.getContent());
        
        System.out.println("4. ✅ 服务验证通过");
        System.out.println("=== 演示完成：Core模块完全独立运行 ===");
    }
    
    @Test
    public void testMultipleAppsConfiguration() {
        // 测试多应用配置
        WeChatNoticeProperties.AppConfig app1 = new WeChatNoticeProperties.AppConfig();
        app1.setCorpId("corp1");
        app1.setSecret("secret1");
        app1.setAgentId(1000001);
        app1.setToken("token1");
        app1.setAesKey("key1234567890123456789012345678901234567890123456");
        
        WeChatNoticeProperties.AppConfig app2 = new WeChatNoticeProperties.AppConfig();
        app2.setCorpId("corp2");
        app2.setSecret("secret2");
        app2.setAgentId(1000002);
        app2.setToken("token2");
        app2.setAesKey("key2234567890123456789012345678901234567890123456");
        
        WeChatNoticeProperties config = WeChatConfigBuilder.create()
                .defaultApp("app1")
                .addApp("app1", app1)
                .addApp("app2", app2)
                .build();
        
        WeChatAppConfigService appConfigService = new WeChatAppConfigServiceImpl(config);
        
        assertTrue(appConfigService.hasApp("app1"));
        assertTrue(appConfigService.hasApp("app2"));
        assertEquals(2, appConfigService.getAllApps().size());
        
        System.out.println("✅ 多应用配置测试通过");
    }
}