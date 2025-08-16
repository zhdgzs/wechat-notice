# WeChat Notice - 企业微信消息通知组件

[![Maven Central](https://img.shields.io/badge/maven--central-1.0.1-blue.svg)](https://search.maven.org/artifact/io.github.zhdgzs/wechat-notice-spring-boot-starter)
[![Java Version](https://img.shields.io/badge/Java-8%2B-brightgreen.svg)](https://www.oracle.com/java/technologies/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x%20%7C%203.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

> 🚀 **开箱即用的企业微信消息推送组件**，让应用监控、告警、通知触手可及

基于企业微信应用消息API的轻量级通知组件，支持多种消息类型，完美集成Spring Boot生态。通过企业微信应用发送消息，用户可在微信中直接接收，无需额外安装客户端。

## 演示站
> https://qynotice.fu520.top/


## 🎯 核心优势

- **🔧 开箱即用** - Spring Boot Starter，零配置启动
- **🎨 消息丰富** - 支持文本、Markdown、图片、卡片等多种消息格式  
- **⚡ 高性能** - 内置连接池、Token自动管理、故障重试机制
- **🔒 企业级** - 支持多应用配置、API验证、消息加密
- **🌐 网络友好** - 可配置代理，适应各种网络环境
- **📱 移动优先** - 消息直达微信，随时随地接收通知

## 💡 典型场景

- **系统监控** - 服务异常、性能告警、业务指标监控
- **业务通知** - 订单状态、支付回调、审核结果通知  
- **运维告警** - 服务宕机、资源不足、部署状态推送
- **个人项目** - 任务完成、数据采集、定时任务结果通知

## ✨ 特性

- 🚀 **开箱即用**: SpringBoot Starter，简单配置即可使用
- 🔧 **多应用支持**: 支持配置多个企业微信应用，灵活切换
- 🔐 **API验证**: 内置Portal控制器，自动处理微信接口验证
- 📱 **多消息类型**: 支持文本、图片、Markdown、卡片等多种消息类型
- 🏗️ **Builder模式**: 简洁的消息构建器，优雅构建复杂消息
- ⚡ **高性能**: HTTP连接池、Token自动缓存和刷新
- 🌐 **代理支持**: 可配置API代理地址，解决动态IP需要频繁配置企业微信IP白名单，和内网网络访问问题

## 📦 项目结构

```
wechat-notice-parent/
├── wechat-notice-core/                         # 核心功能模块
├── wechat-notice-spring-boot-starter/          # Spring Boot 2.x 启动器  
├── wechat-notice-spring-boot-3-starter/        # Spring Boot 3.x 启动器
└── wechat-notice-example/                      # 使用示例
```

## 🛠️ 技术栈

- **JDK**: 8+，17, 21
- **Spring Boot**: 2.x、3.x
- **Jackson**: JSON序列化/反序列化
- **Lombok**: 代码简化
- **Apache HttpClient**: HTTP客户端

## 🚀 快速开始

### 1. 添加依赖

#### Spring Boot 2.x

```xml
<dependency>
    <groupId>io.github.zhdgzs</groupId>
    <artifactId>wechat-notice-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

#### Spring Boot 3.x

```xml
<dependency>
    <groupId>io.github.zhdgzs</groupId>
    <artifactId>wechat-notice-spring-boot-3-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 2. 配置文件

```yaml
wechat:
  notice:
    enabled: true
    default-app: prod
    portal:
      enabled: true
      path: /wx/cp/portal
    # API配置（可选）
    api:
      base-url: https://qyapi.weixin.qq.com  # 可配置为代理地址
      connect-timeout: 10000
      read-timeout: 30000
      retry-enabled: true
      retry-count: 3
    apps:
      prod:
        corp-id: ww123456789abcdef
        secret: your-secret-here
        agent-id: 1000001
        token: your-token-here
        aes-key: your-aes-key-here
      test:
        corp-id: ww987654321fedcba
        secret: test-secret-here
        agent-id: 1000002
        token: test-token-here
        aes-key: test-aes-key-here
```

### 3. 使用示例

#### 简单文本消息

```java
@Autowired
private WeChatNoticeService weChatNoticeService;

// 使用默认应用发送
WeChatMessageResult result = weChatNoticeService.sendText("Hello World!", "user123");

// 指定应用发送
WeChatMessageResult result = weChatNoticeService.sendText("Hello World!", "user123", "test");
```

#### Markdown消息

```java
String markdown = """
    # 重要通知 📢
    
    **内容**: 系统将于今晚进行维护
    **时间**: 22:00 - 02:00
    
    [查看详情](http://example.com)
    """;

weChatNoticeService.sendMarkdown(markdown, "@all");
```

#### 使用构建器

```java
WeChatMessage message = WeChatMessageBuilder.text()
    .content("这是一条重要通知")
    .toUser("user1|user2|user3")
    .toParty("1|2")
    .build();

WeChatMessageResult result = weChatNoticeService.sendMessage(message, "prod");
```

#### 卡片消息

```java
WeChatMessage message = WeChatMessageBuilder.textCard()
    .title("任务提醒")
    .description("您有新的任务需要处理")
    .url("http://example.com/task/123")
    .btnTxt("查看任务")
    .toUser("user123")
    .build();

weChatNoticeService.sendMessage(message);
```

#### 图文消息

```java
WeChatMessage message = WeChatMessageBuilder.news()
    .addArticle("新版本发布", "我们发布了新版本，包含多项功能改进", 
                "http://example.com/news/1", 
                "http://example.com/images/news1.jpg")
    .addArticle("技术分享", "Spring Boot最佳实践分享", 
                "http://example.com/news/2", 
                "http://example.com/images/news2.jpg")
    .toUser("@all")
    .build();

weChatNoticeService.sendMessage(message);
```

## 📋 配置说明

### 基础配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `wechat.notice.enabled` | Boolean | true | 是否启用微信通知功能 |
| `wechat.notice.default-app` | String | default | 默认应用名称 |

### Portal配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `wechat.notice.portal.enabled` | Boolean | true | 是否启用Portal控制器 |
| `wechat.notice.portal.path` | String | /wx/cp/portal | Portal接口路径 |

### API配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `wechat.notice.api.base-url` | String | https://qyapi.weixin.qq.com | 企业微信API基础URL |
| `wechat.notice.api.connect-timeout` | Integer | 10000 | 连接超时时间（毫秒） |
| `wechat.notice.api.read-timeout` | Integer | 30000 | 读取超时时间（毫秒） |
| `wechat.notice.api.retry-enabled` | Boolean | true | 是否启用重试机制 |
| `wechat.notice.api.retry-count` | Integer | 3 | 重试次数 |

### 应用配置

每个应用需要配置以下参数：

| 配置项 | 类型 | 说明 |
|-------|------|------|
| `corp-id` | String | 企业ID |
| `secret` | String | 应用Secret |
| `agent-id` | Integer | 应用ID |
| `token` | String | Token（用于接口验证） |
| `aes-key` | String | AES Key（用于消息加解密） |

## 🔌 API验证

组件内置Portal控制器，自动处理微信服务器的接口验证：

- **验证地址**: `http://your-domain{portal.path}/{appName}`
- **示例**: `http://example.com/wx/cp/portal/prod`

在企业微信管理后台配置接收消息地址时，使用上述地址即可。

## 🌐 代理配置

如果你的服务器无法直接访问企业微信API，可以通过配置代理服务器来解决：

### 方式一：HTTP代理服务器

```yaml
wechat:
  notice:
    api:
      base-url: http://your-proxy.com/wechat-api
```

### 方式二：内网代理转发

```yaml
wechat:
  notice:
    api:
      base-url: http://internal-proxy.company.com:8080
```

### 方式三：Nginx代理配置

如果使用Nginx作为代理，可参考以下配置：

```nginx
location /wechat-api/ {
    proxy_pass https://qyapi.weixin.qq.com/;
    proxy_set_header Host qyapi.weixin.qq.com;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

然后配置：

```yaml
wechat:
  notice:
    api:
      base-url: http://your-server.com/wechat-api
```

## 📊 消息类型支持

| 消息类型 | 说明 | 构建器支持 | 示例用途                                  |
|---------|------|----------|---------------------------------------|
| text | 文本消息 | ✅ | 简单通知、状态更新                             |
| textcard | 文本卡片消息 | ✅ | 任务提醒、链接分享、自定义网页                |
| news | 图文消息 | ✅ | 新闻推送、公告、多图文支持                      |
| markdown | Markdown消息 | ❌ | 格式化通知、文档 (微信不支持，可以放到 textcard 中自定义实现) |
| file | 文件消息 | ❌ | 暂不支持                                  |
| image | 图片消息 | ❌ | 暂不支持                                  |

**因为文件和图片消息，需要先上传文件到企业微信的临时素材接口，所以暂不支持发送文件、图片消息。**

## 🔧 高级功能

### 批量发送

```java
List<WeChatMessage> messages = Arrays.asList(
    WeChatMessageBuilder.text().content("消息1").toUser("user1").build(),
    WeChatMessageBuilder.text().content("消息2").toUser("user2").build()
);

List<WeChatMessageResult> results = weChatNoticeService.batchSendMessage(messages, "prod");
```

### 异常处理

```java
try {
    WeChatMessageResult result = weChatNoticeService.sendText("测试消息", "user123");
    if (!result.isSuccess()) {
        log.error("消息发送失败: {}", result.getErrMsg());
    }
} catch (WeChatNoticeException e) {
    log.error("微信通知异常", e);
}
```

## ⚡ 性能优化与最佳实践

### 连接池配置
组件内置HTTP连接池，默认配置已优化，支持以下配置调整：

```yaml
wechat:
  notice:
    api:
      connect-timeout: 5000      # 连接超时（毫秒）
      read-timeout: 30000        # 读取超时（毫秒）
      retry-count: 3             # 重试次数
```

### Token缓存机制
- Access Token自动缓存，有效期内复用
- 过期前自动刷新，避免请求失败
- 支持多应用独立Token管理

### 批量发送建议
- 单次批量发送建议不超过100条消息
- 大量消息可分批处理，避免API限流
- 异步处理提升用户体验

### 错误处理最佳实践

```java
@Service
public class NotificationService {
    
    @Autowired
    private WeChatNoticeService weChatNoticeService;
    
    @Retryable(maxAttempts = 3)
    public void sendNotificationWithRetry(String message, String user) {
        try {
            WeChatMessageResult result = weChatNoticeService.sendText(message, user);
            if (!result.isSuccess()) {
                throw new RuntimeException("发送失败: " + result.getErrMsg());
            }
        } catch (WeChatNoticeException e) {
            log.error("微信通知发送异常", e);
            throw e;
        }
    }
}
```

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## ❓ 常见问题

### 1. 如何获取企业微信配置参数？

- **企业ID**: 企业微信管理后台 -> 我的企业 -> 企业信息
- **应用Secret**: 企业微信管理后台 -> 应用管理 -> 自建应用 -> 查看Secret
- **应用ID**: 企业微信管理后台 -> 应用管理 -> 自建应用 -> AgentId
- **Token/AESKey**: 企业微信管理后台 -> 应用管理 -> 接收消息 -> 设置API接收

### 2. 消息发送失败怎么办？

1. 检查配置参数是否正确
2. 确认接收用户在企业微信应用的可见范围内
3. 查看日志获取详细错误信息
4. 参考企业微信官方文档的错误码说明

### 3. 如何自定义Portal路径？

```yaml
wechat:
  notice:
    portal:
      path: /custom/wx/portal  # 自定义路径
```

### 4. 如何禁用Portal功能？

```yaml
wechat:
  notice:
    portal:
      enabled: false
```

### 5. 如何配置代理访问企业微信API？

如果服务器无法直接访问企业微信API，可以配置代理：

```yaml
wechat:
  notice:
    api:
      base-url: http://your-proxy.com/wechat-api
```

### 6. 代理服务器需要转发哪些接口？

主要需要转发以下企业微信API接口：
- `/cgi-bin/gettoken` - 获取访问令牌
- `/cgi-bin/message/send` - 发送消息

确保代理服务器能正确转发这些路径到 `https://qyapi.weixin.qq.com`。

### 7. Spring Boot 2.x 和 3.x 如何选择？

- **Spring Boot 2.x** 项目使用 `wechat-notice-spring-boot-starter`
- **Spring Boot 3.x** 项目使用 `wechat-notice-spring-boot-3-starter`  
- 两个版本功能完全一致，仅适配了不同的Spring Boot版本

### 8. 如何在非Spring Boot项目中使用？

可以直接使用 `wechat-notice-core` 模块：

```xml
<dependency>
    <groupId>io.github.zhdgzs</groupId>
    <artifactId>wechat-notice-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

然后手动配置和使用服务类，在 `wechat-notice-core` 模块的 `test` 包下有示例代码。

## 🔗 相关链接

- [企业微信API文档](https://developer.work.weixin.qq.com/)
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [WxJava](https://github.com/Wechat-Group/WxJava)

---

如有问题或建议，欢迎提交 [Issue](../../issues) 或 [Pull Request](../../pulls)！