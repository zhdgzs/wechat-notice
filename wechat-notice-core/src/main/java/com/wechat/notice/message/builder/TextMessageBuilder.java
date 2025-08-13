package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * 文本消息构建器
 * 
 * @author WeChat Notice
 */
public class TextMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<TextMessageBuilder> {
    
    public TextMessageBuilder() {
        message.setMsgType(MessageType.TEXT);
    }
    
    public TextMessageBuilder content(String content) {
        message.setContent(content);
        return this;
    }
}