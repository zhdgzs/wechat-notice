package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * 文本卡片消息构建器
 * 
 * @author fyf
 */
public class TextCardMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<TextCardMessageBuilder> {
    
    public TextCardMessageBuilder() {
        message.setMsgType(MessageType.TEXTCARD);
    }
    
    public TextCardMessageBuilder title(String title) {
        message.setTitle(title);
        return this;
    }
    
    public TextCardMessageBuilder description(String description) {
        message.setDescription(description);
        return this;
    }
    
    public TextCardMessageBuilder url(String url) {
        message.setUrl(url);
        return this;
    }
    
    public TextCardMessageBuilder btnTxt(String btnTxt) {
        message.setBtnTxt(btnTxt);
        return this;
    }
}