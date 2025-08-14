package com.wechat.notice.message.enums;

import lombok.Getter;

/**
 * 微信消息类型枚举
 * 
 * @author fyf
 */
@Getter
public enum MessageType {
    /**
     * 文本消息
     */
    TEXT("text", "文本消息"),
    
    /**
     * 图片消息
     */
    IMAGE("image", "图片消息"),
    
    /**
     * 语音消息
     */
    VOICE("voice", "语音消息"),
    
    /**
     * 视频消息
     */
    VIDEO("video", "视频消息"),
    
    /**
     * 文件消息
     */
    FILE("file", "文件消息"),
    
    /**
     * 文本卡片消息
     */
    TEXTCARD("textcard", "文本卡片消息"),
    
    /**
     * 图文消息
     */
    NEWS("news", "图文消息"),
    
    /**
     * 图文消息(mpnews)
     */
    MPNEWS("mpnews", "图文消息"),
    
    /**
     * Markdown消息
     */
    MARKDOWN("markdown", "Markdown消息"),
    
    /**
     * 模板卡片消息
     */
    TEMPLATE_CARD("template_card", "模板卡片消息");
    
    private final String code;
    private final String description;
    
    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}