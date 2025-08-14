package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * Markdown消息构建器
 * 
 * @author fyf
 */
public class MarkdownMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<MarkdownMessageBuilder> {
    
    public MarkdownMessageBuilder() {
        message.setMsgType(MessageType.MARKDOWN);
    }
    
    public MarkdownMessageBuilder content(String content) {
        message.setContent(content);
        return this;
    }
}