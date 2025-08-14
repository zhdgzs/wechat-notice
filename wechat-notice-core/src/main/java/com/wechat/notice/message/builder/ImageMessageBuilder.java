package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * 图片消息构建器
 * 
 * @author fyf
 */
public class ImageMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<ImageMessageBuilder> {
    
    public ImageMessageBuilder() {
        message.setMsgType(MessageType.IMAGE);
    }
    
    public ImageMessageBuilder mediaId(String mediaId) {
        message.setMediaId(mediaId);
        return this;
    }
}