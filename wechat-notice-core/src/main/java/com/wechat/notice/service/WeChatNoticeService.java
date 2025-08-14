package com.wechat.notice.service;

import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.WeChatMessageResult;

import java.util.List;

/**
 * 微信通知服务接口
 * 
 * @author fyf
 */
public interface WeChatNoticeService {
    
    /**
     * 发送文本消息（使用默认应用）
     *
     * @param content 消息内容
     * @param toUser  接收用户ID
     * @return 发送结果
     */
    WeChatMessageResult sendText(String content, String toUser);
    
    /**
     * 发送文本消息（指定应用）
     *
     * @param content 消息内容
     * @param toUser  接收用户ID
     * @param appName 应用名称
     * @return 发送结果
     */
    WeChatMessageResult sendText(String content, String toUser, String appName);
    
    /**
     * 发送消息（使用默认应用）
     *
     * @param message 消息对象
     * @return 发送结果
     */
    WeChatMessageResult sendMessage(WeChatMessage message);
    
    /**
     * 发送消息（指定应用）
     *
     * @param message 消息对象
     * @param appName 应用名称
     * @return 发送结果
     */
    WeChatMessageResult sendMessage(WeChatMessage message, String appName);
    
    /**
     * 批量发送消息
     *
     * @param messages 消息列表
     * @param appName  应用名称
     * @return 发送结果列表
     */
    List<WeChatMessageResult> batchSendMessage(List<WeChatMessage> messages, String appName);
    
    /**
     * 发送Markdown消息
     *
     * @param content Markdown内容
     * @param toUser  接收用户ID
     * @return 发送结果
     */
    WeChatMessageResult sendMarkdown(String content, String toUser);
    
    /**
     * 发送Markdown消息（指定应用）
     *
     * @param content Markdown内容
     * @param toUser  接收用户ID
     * @param appName 应用名称
     * @return 发送结果
     */
    WeChatMessageResult sendMarkdown(String content, String toUser, String appName);
}