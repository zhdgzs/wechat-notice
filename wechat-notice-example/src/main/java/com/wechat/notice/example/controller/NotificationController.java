package com.wechat.notice.example.controller;

import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.WeChatMessageResult;
import com.wechat.notice.message.builder.WeChatMessageBuilder;
import com.wechat.notice.service.WeChatNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器示例
 * 
 * @author WeChat Notice
 */
@Slf4j
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    
    private final WeChatNoticeService weChatNoticeService;
    
    /**
     * 发送简单文本消息
     */
    @PostMapping("/send/text")
    public String sendText(@RequestParam String content, 
                          @RequestParam String toUser,
                          @RequestParam(required = false) String appName) {
        try {
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendText(content, toUser, appName);
            } else {
                result = weChatNoticeService.sendText(content, toUser);
            }
            
            if (result.isSuccess()) {
                return "消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送文本消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
    
    /**
     * 发送Markdown消息
     */
    @PostMapping("/send/markdown")
    public String sendMarkdown(@RequestParam String content, 
                              @RequestParam String toUser,
                              @RequestParam(required = false) String appName) {
        try {
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMarkdown(content, toUser, appName);
            } else {
                result = weChatNoticeService.sendMarkdown(content, toUser);
            }
            
            if (result.isSuccess()) {
                return "Markdown消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "Markdown消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送Markdown消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
    
    /**
     * 发送复杂消息
     */
    @PostMapping("/send/complex")
    public String sendComplexMessage(@RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String url,
                                   @RequestParam String toUser,
                                   @RequestParam(required = false) String appName) {
        try {
            WeChatMessage message = WeChatMessage.builder()
                .msgType("textcard")
                .title(title)
                .description(description)
                .url(url)
                .btnTxt("查看详情")
                .toUser(toUser)
                .build();
                
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMessage(message, appName);
            } else {
                result = weChatNoticeService.sendMessage(message);
            }
            
            if (result.isSuccess()) {
                return "复杂消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "复杂消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送复杂消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
    
    /**
     * 使用构建器发送消息
     */
    @PostMapping("/send/builder")
    public String sendBuilderMessage(@RequestParam String content,
                                   @RequestParam String users,
                                   @RequestParam(required = false) String appName) {
        try {
            WeChatMessage message = WeChatMessageBuilder.text()
                .content(content)
                .toUser(users)
                .build();
                
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMessage(message, appName);
            } else {
                result = weChatNoticeService.sendMessage(message);
            }
            
            if (result.isSuccess()) {
                return "构建器消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "构建器消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送构建器消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
    
    /**
     * 发送全员通知
     */
    @PostMapping("/send/all")
    public String sendToAll(@RequestParam String content,
                           @RequestParam(required = false) String appName) {
        try {
            WeChatMessage message = WeChatMessageBuilder.text()
                .content(content)
                .toAll()
                .build();
                
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMessage(message, appName);
            } else {
                result = weChatNoticeService.sendMessage(message);
            }
            
            if (result.isSuccess()) {
                return "全员通知发送成功，消息ID: " + result.getMsgId();
            } else {
                return "全员通知发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送全员通知异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
}