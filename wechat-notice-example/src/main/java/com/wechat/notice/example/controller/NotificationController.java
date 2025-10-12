package com.wechat.notice.example.controller;

import com.wechat.notice.example.dto.SendTextRequest;
import com.wechat.notice.example.dto.SendMarkdownRequest;
import com.wechat.notice.example.dto.SendNewsRequest;
import com.wechat.notice.example.dto.SendTextCardRequest;
import com.wechat.notice.example.dto.SendToAllRequest;
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

    @PostMapping("/send/text2")
    public String sendText2(@RequestBody String request) {
        System.out.println( request);
        return "";
    }
    /**
     * 发送简单文本消息
     */
    @PostMapping("/send/text")
    public String sendText(@RequestBody SendTextRequest request) {
        try {
            WeChatMessageResult result;
            if (request.getAppName() != null) {
                result = weChatNoticeService.sendText(request.getContent(), request.getToUser(), request.getAppName());
            } else {
                result = weChatNoticeService.sendText(request.getContent(), request.getToUser());
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
    public String sendMarkdown(@RequestBody SendMarkdownRequest request) {
        try {
            WeChatMessageResult result;
            if (request.getAppName() != null) {
                result = weChatNoticeService.sendMarkdown(request.getContent(), request.getToUser(), request.getAppName());
            } else {
                result = weChatNoticeService.sendMarkdown(request.getContent(), request.getToUser());
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
    public String sendNews(@RequestBody SendNewsRequest request) {
        try {
            WeChatMessage message = WeChatMessageBuilder.news()
                .addArticle(request.getTitle(), request.getDescription(), request.getUrl(), request.getPicUrl())
                .toUser(request.getToUser())
                .build();
                
            WeChatMessageResult result;
            if (request.getAppName() != null) {
                result = weChatNoticeService.sendMessage(message, request.getAppName());
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
    public String sendTextCard(@RequestBody SendTextCardRequest request) {
        try {
            WeChatMessage message = WeChatMessageBuilder.textCard()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(request.getUrl())
                .toUser(request.getToUser())
                .build();
                
            WeChatMessageResult result;
            if (request.getAppName() != null) {
                result = weChatNoticeService.sendMessage(message, request.getAppName());
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
    public String sendToAll(@RequestBody SendToAllRequest request) {
        try {
            WeChatMessage message = WeChatMessageBuilder.text()
                .content(request.getContent())
                .toAll()
                .build();
                
            WeChatMessageResult result;
            if (request.getAppName() != null) {
                result = weChatNoticeService.sendMessage(message, request.getAppName());
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