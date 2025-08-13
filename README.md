# WeChat Notice - 企业微信通知组件

一个轻量级、易集成的企业微信消息发送SpringBoot Starter组件。

## ✨ 特性

- 🚀 **开箱即用**: SpringBoot Starter，一行配置即可使用
- 🔧 **多应用支持**: 支持配置多个企业微信应用，灵活切换
- 🔐 **API验证**: 内置Portal控制器，自动处理微信接口验证
- 📱 **多消息类型**: 支持文本、图片、Markdown、卡片等多种消息类型
- 🏗️ **Builder模式**: 简洁的消息构建器，优雅构建复杂消息
- ⚡ **高性能**: HTTP连接池、Token自动缓存和刷新
- 🌐 **代理支持**: 可配置API代理地址，解决网络访问问题
- 📝 **IDE友好**: 完整的配置智能提示和文档

## 📦 项目结构

```
wechat-notice-parent/
├── wechat-notice-core/                    # 核心功能模块
├── wechat-notice-spring-boot-starter/     # SpringBoot启动器
└── wechat-notice-example/                 # 使用示例
```

## 🛠️ 技术栈

- **JDK**: 8+，17, 21
- **Spring Boot**: 2.x、3.x
- **Jackson**: JSON序列化/反序列化
- **Lombok**: 代码简化
- **Apache HttpClient**: HTTP客户端

## 🚀 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.wechat</groupId>
    <artifactId>wechat-notice-spring-boot-starter</artifactId>
    <version>1.0.0</version>
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
WeChatMessage message = WeChatMessage.builder()
    .msgType("textcard")
    .title("任务提醒")
    .description("您有新的任务需要处理")
    .url("http://example.com/task/123")
    .btnTxt("查看任务")
    .toUser("user123")
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

| 消息类型 | 说明 | 构建器支持 |
|---------|------|----------|
| text | 文本消息 | ✅ |
| image | 图片消息 | ✅ |
| markdown | Markdown消息 | ✅ |
| textcard | 文本卡片消息 | ❌ |
| news | 图文消息 | ❌ |
| file | 文件消息 | ❌ |

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

## 🧪 运行示例

1. 克隆项目并进入目录：
```bash
git clone <repository-url>
cd wechat-notice-parent
```

2. 编译项目：
```bash
mvn clean install
```

3. 配置企业微信参数：
编辑 `wechat-notice-example/src/main/resources/application.yml`，填入实际的企业微信配置。

4. 运行示例：
```bash
cd wechat-notice-example
mvn spring-boot:run
```

5. 测试API：
```bash
# 发送文本消息
curl -X POST "http://localhost:8080/api/notification/send/text" \
  -d "content=测试消息&toUser=@all"

# 发送Markdown消息
curl -X POST "http://localhost:8080/api/notification/send/markdown" \
  -d "content=# 测试标题\n**内容**：这是测试&toUser=@all"
```

## 📝 开发说明

### 构建项目

```bash
mvn clean install
```

### 运行测试

```bash
mvn test
```

### 打包发布

```bash
mvn clean package
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

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

## 🔗 相关链接

- [企业微信API文档](https://developer.work.weixin.qq.com/)
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)

---

如有问题或建议，欢迎提交 [Issue](../../issues) 或 [Pull Request](../../pulls)！