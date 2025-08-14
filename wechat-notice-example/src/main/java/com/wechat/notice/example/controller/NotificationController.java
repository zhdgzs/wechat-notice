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
 * @author fyf
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
    public String sendText(@RequestParam("content") String content, 
                          @RequestParam("toUser") String toUser,
                          @RequestParam(value = "appName", required = false) String appName) {
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
    public String sendMarkdown(@RequestParam("content") String content, 
                              @RequestParam("toUser") String toUser,
                              @RequestParam(value = "appName", required = false) String appName) {
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
     * 发送图文消息
     */
    @PostMapping("/send/news")
    public String sendNews(@RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("url") String url,
                          @RequestParam("picUrl") String picUrl,
                          @RequestParam("toUser") String toUser,
                          @RequestParam(value = "appName", required = false) String appName) {
        try {
            WeChatMessage message = WeChatMessageBuilder.news()
                .addArticle(title, description, url, picUrl)
                .toUser(toUser)
                .build();
                
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMessage(message, appName);
            } else {
                result = weChatNoticeService.sendMessage(message);
            }
            
            if (result.isSuccess()) {
                return "图文消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "图文消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送图文消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }
    
    /**
     * 发送TextCard消息
     */
    @PostMapping("/send/textcard")
    public String sendTextCard(@RequestParam("title") String title,
                              @RequestParam("description") String description,
                              @RequestParam("url") String url,
                              @RequestParam("toUser") String toUser,
                              @RequestParam(value = "appName", required = false) String appName) {
        try {
            WeChatMessage message = WeChatMessageBuilder.textCard()
                .title(title)
                .description(description)
                .url(url)
                .toUser(toUser)
                .build();
                
            WeChatMessageResult result;
            if (appName != null) {
                result = weChatNoticeService.sendMessage(message, appName);
            } else {
                result = weChatNoticeService.sendMessage(message);
            }
            
            if (result.isSuccess()) {
                return "TextCard消息发送成功，消息ID: " + result.getMsgId();
            } else {
                return "TextCard消息发送失败: " + result.getErrMsg();
            }
        } catch (Exception e) {
            log.error("发送TextCard消息异常", e);
            return "发送异常: " + e.getMessage();
        }
    }

    /**
     * 发送全员通知
     */
    @PostMapping("/send/all")
    public String sendToAll(@RequestParam("content") String content,
                           @RequestParam(value = "appName", required = false) String appName) {
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