package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * Markdown消息构建器
 * 
 * @author fyf
 */
public class MarkdownMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<MarkdownMessageBuilder> {
    
    /**
     * 构造方法，初始化消息类型为Markdown
     */
    public MarkdownMessageBuilder() {
        message.setMsgType(MessageType.MARKDOWN);
    }
    
    /**
     * 设置Markdown内容
     *
     * @param content Markdown格式的文本内容
     * @return 构建器实例
     */
    public MarkdownMessageBuilder content(String content) {
        message.setContent(content);
        return this;
    }
}