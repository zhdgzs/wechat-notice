package com.wechat.notice.message.builder;

import com.wechat.notice.message.WeChatMessage;

/**
 * 微信消息构建器工厂
 * 
 * @author fyf
 */
public class WeChatMessageBuilder {
    
    /**
     * 创建文本消息构建器
     *
     * @return 文本消息构建器
     */
    public static TextMessageBuilder text() {
        return new TextMessageBuilder();
    }

    /**
     * 创建文本卡片消息构建器
     *
     * @return 文本卡片消息构建器
     */
    public static TextCardMessageBuilder textCard() {
        return new TextCardMessageBuilder();
    }

    /**
     * 创建图文消息构建器
     *
     * @return 图文消息构建器
     */
    public static NewsMessageBuilder news() {
        return new NewsMessageBuilder();
    }

    /**
     * 创建Markdown消息构建器
     *
     * @return Markdown消息构建器
     */
    public static MarkdownMessageBuilder markdown() {
        return new MarkdownMessageBuilder();
    }
    
    /**
     * 基础消息构建器
     *
     * @param <T> 构建器类型
     */
    public static abstract class BaseMessageBuilder<T extends BaseMessageBuilder<T>> {
        /**
         * 微信消息对象
         */
        protected WeChatMessage message = new WeChatMessage();
        
        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }
        
        /**
         * 设置接收用户
         *
         * @param toUser 用户ID，多个用'|'分隔
         * @return 构建器实例
         */
        public T toUser(String toUser) {
            message.setToUser(toUser);
            return self();
        }
        
        /**
         * 设置接收部门
         *
         * @param toParty 部门ID，多个用'|'分隔
         * @return 构建器实例
         */
        public T toParty(String toParty) {
            message.setToParty(toParty);
            return self();
        }
        
        /**
         * 设置接收标签
         *
         * @param toTag 标签ID，多个用'|'分隔
         * @return 构建器实例
         */
        public T toTag(String toTag) {
            message.setToTag(toTag);
            return self();
        }
        
        /**
         * 设置发送给所有人
         *
         * @return 构建器实例
         */
        public T toAll() {
            message.setToUser("@all");
            return self();
        }
        
        /**
         * 构建消息对象
         *
         * @return 微信消息对象
         */
        public WeChatMessage build() {
            return message;
        }
    }
}