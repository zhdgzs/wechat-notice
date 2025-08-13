package com.wechat.notice.service.impl;

import com.wechat.notice.client.WeChatApiClient;
import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.WeChatMessageResult;
import com.wechat.notice.message.enums.MessageType;
import com.wechat.notice.service.WeChatAppConfigService;
import com.wechat.notice.service.WeChatNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信通知服务实现
 * 
 * @author WeChat Notice
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatNoticeServiceImpl implements WeChatNoticeService {
    
    private final WeChatApiClient apiClient;
    private final WeChatAppConfigService appConfigService;
    private final WeChatNoticeProperties properties;
    
    @Override
    public WeChatMessageResult sendText(String content, String toUser) {
        return sendText(content, toUser, properties.getDefaultApp());
    }
    
    @Override
    public WeChatMessageResult sendText(String content, String toUser, String appName) {
        WeChatMessage message = WeChatMessage.builder()
            .msgType(MessageType.TEXT.getCode())
            .content(content)
            .toUser(toUser)
            .build();
        return sendMessage(message, appName);
    }
    
    @Override
    public WeChatMessageResult sendMessage(WeChatMessage message) {
        return sendMessage(message, properties.getDefaultApp());
    }
    
    @Override
    public WeChatMessageResult sendMessage(WeChatMessage message, String appName) {
        try {
            log.debug("开始发送微信消息: appName={}, msgType={}, toUser={}", 
                appName, message.getMsgType(), message.getToUser());
            
            WeChatAppConfig appConfig = appConfigService.getApp(appName);
            String accessToken = apiClient.getAccessToken(appConfig);
            
            // 设置agentId
            message.setAgentId(appConfig.getAgentId());
            
            // 发送消息
            WeChatMessageResult result = apiClient.sendMessage(accessToken, message);
            
            if (result.isSuccess()) {
                log.info("微信消息发送成功: appName={}, msgId={}", appName, result.getMsgId());
            } else {
                log.warn("微信消息发送失败: appName={}, errCode={}, errMsg={}", 
                    appName, result.getErrCode(), result.getErrMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("发送微信消息异常: appName={}, message={}", appName, message, e);
            return WeChatMessageResult.failure(-1, "发送消息异常: " + e.getMessage());
        }
    }
    
    @Override
    public List<WeChatMessageResult> batchSendMessage(List<WeChatMessage> messages, String appName) {
        List<WeChatMessageResult> results = new ArrayList<>();
        
        if (messages == null || messages.isEmpty()) {
            log.warn("批量发送消息列表为空");
            return results;
        }
        
        log.info("开始批量发送微信消息: appName={}, count={}", appName, messages.size());
        
        for (int i = 0; i < messages.size(); i++) {
            try {
                WeChatMessage message = messages.get(i);
                WeChatMessageResult result = sendMessage(message, appName);
                results.add(result);
                
                // 简单的频率控制，避免请求过快
                if (i < messages.size() - 1) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("批量发送消息被中断");
                break;
            } catch (Exception e) {
                log.error("批量发送消息异常: index={}", i, e);
                results.add(WeChatMessageResult.failure(-1, "发送异常: " + e.getMessage()));
            }
        }
        
        long successCount = results.stream().mapToLong(r -> r.isSuccess() ? 1 : 0).sum();
        log.info("批量发送微信消息完成: appName={}, total={}, success={}", 
            appName, results.size(), successCount);
        
        return results;
    }
    
    @Override
    public WeChatMessageResult sendMarkdown(String content, String toUser) {
        return sendMarkdown(content, toUser, properties.getDefaultApp());
    }
    
    @Override
    public WeChatMessageResult sendMarkdown(String content, String toUser, String appName) {
        WeChatMessage message = WeChatMessage.builder()
            .msgType(MessageType.MARKDOWN.getCode())
            .content(content)
            .toUser(toUser)
            .build();
        return sendMessage(message, appName);
    }
}