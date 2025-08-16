package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * 文本消息构建器
 * 
 * @author fyf
 */
public class TextMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<TextMessageBuilder> {
    
    /**
     * 构造方法，初始化消息类型为文本
     */
    public TextMessageBuilder() {
        message.setMsgType(MessageType.TEXT);
    }
    
    /**
     * 设置文本内容
     *
     * @param content 文本内容，最长不超过2048个字节
     * @return 构建器实例
     */
    public TextMessageBuilder content(String content) {
        message.setContent(content);
        return this;
    }
}