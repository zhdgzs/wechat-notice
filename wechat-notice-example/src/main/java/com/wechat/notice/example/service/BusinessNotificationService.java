package com.wechat.notice.example.service;

import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.WeChatMessageResult;
import com.wechat.notice.message.builder.WeChatMessageBuilder;
import com.wechat.notice.service.WeChatNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 业务通知服务示例
 * 
 * @author fyf
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessNotificationService {
    
    private final WeChatNoticeService weChatNoticeService;
    
    /**
     * 发送系统告警
     */
    public void sendSystemAlert(String alertMessage) {
        String markdown = String.format("""
            # 系统告警 🚨
            
            **时间**: %s
            **内容**: %s
            
            请及时处理！
            """, 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            alertMessage);
            
        WeChatMessageResult result = weChatNoticeService.sendMarkdown(markdown, "@all");
        
        if (result.isSuccess()) {
            log.info("系统告警发送成功: msgId={}", result.getMsgId());
        } else {
            log.error("系统告警发送失败: {}", result.getErrMsg());
        }
    }
    
    /**
     * 发送业务通知（指定应用）
     */
    public void sendBusinessNotification(String content, String userId, String appName) {
        WeChatMessage message = WeChatMessageBuilder.text()
            .content(content)
            .toUser(userId)
            .build();
            
        WeChatMessageResult result = weChatNoticeService.sendMessage(message, appName);
        
        if (!result.isSuccess()) {
            log.error("业务通知发送失败: userId={}, appName={}, error={}", 
                userId, appName, result.getErrMsg());
        }
    }
    
    /**
     * 发送部门通知
     */
    public void sendDepartmentNotification(String content, String departmentId) {
        WeChatMessage message = WeChatMessage.builder()
            .msgType("text")
            .content(content)
            .toParty(departmentId)
            .build();
            
        WeChatMessageResult result = weChatNoticeService.sendMessage(message);
        
        if (result.isSuccess()) {
            log.info("部门通知发送成功: departmentId={}, msgId={}", departmentId, result.getMsgId());
        } else {
            log.error("部门通知发送失败: departmentId={}, error={}", departmentId, result.getErrMsg());
        }
    }
    
    /**
     * 发送任务卡片通知
     */
    public void sendTaskCardNotification(String title, String description, String url, String userId) {
        WeChatMessage message = WeChatMessage.builder()
            .msgType("textcard")
            .title(title)
            .description(description)
            .url(url)
            .btnTxt("查看任务")
            .toUser(userId)
            .build();
            
        WeChatMessageResult result = weChatNoticeService.sendMessage(message);
        
        if (result.isSuccess()) {
            log.info("任务卡片通知发送成功: userId={}, msgId={}", userId, result.getMsgId());
        } else {
            log.error("任务卡片通知发送失败: userId={}, error={}", userId, result.getErrMsg());
        }
    }
}