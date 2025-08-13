package com.wechat.notice.message.builder;

import com.wechat.notice.message.WeChatMessage;

/**
 * 微信消息构建器工厂
 * 
 * @author WeChat Notice
 */
public class WeChatMessageBuilder {
    
    /**
     * 创建文本消息构建器
     */
    public static TextMessageBuilder text() {
        return new TextMessageBuilder();
    }
    
    /**
     * 创建图片消息构建器
     */
    public static ImageMessageBuilder image() {
        return new ImageMessageBuilder();
    }
    
    /**
     * 创建Markdown消息构建器
     */
    public static MarkdownMessageBuilder markdown() {
        return new MarkdownMessageBuilder();
    }
    
    /**
     * 基础消息构建器
     */
    public static abstract class BaseMessageBuilder<T extends BaseMessageBuilder<T>> {
        protected WeChatMessage message = new WeChatMessage();
        
        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }
        
        public T toUser(String toUser) {
            message.setToUser(toUser);
            return self();
        }
        
        public T toParty(String toParty) {
            message.setToParty(toParty);
            return self();
        }
        
        public T toTag(String toTag) {
            message.setToTag(toTag);
            return self();
        }
        
        public T toAll() {
            message.setToUser("@all");
            return self();
        }
        
        public WeChatMessage build() {
            return message;
        }
    }
}