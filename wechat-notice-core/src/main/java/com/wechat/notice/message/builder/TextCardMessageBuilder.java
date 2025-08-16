package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;

/**
 * 文本卡片消息构建器
 * 
 * @author fyf
 */
public class TextCardMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<TextCardMessageBuilder> {
    
    /**
     * 构造方法，初始化消息类型为文本卡片
     */
    public TextCardMessageBuilder() {
        message.setMsgType(MessageType.TEXTCARD);
    }
    
    /**
     * 设置卡片标题
     *
     * @param title 标题，不超过128个字节
     * @return 构建器实例
     */
    public TextCardMessageBuilder title(String title) {
        message.setTitle(title);
        return this;
    }
    
    /**
     * 设置卡片描述
     *
     * @param description 描述，不超过512个字节
     * @return 构建器实例
     */
    public TextCardMessageBuilder description(String description) {
        message.setDescription(description);
        return this;
    }
    
    /**
     * 设置点击后跳转的链接
     *
     * @param url 链接地址
     * @return 构建器实例
     */
    public TextCardMessageBuilder url(String url) {
        message.setUrl(url);
        return this;
    }
    
    /**
     * 设置按钮文字
     *
     * @param btnTxt 按钮文字，默认为"详情"，不超过4个文字
     * @return 构建器实例
     */
    public TextCardMessageBuilder btnTxt(String btnTxt) {
        message.setBtnTxt(btnTxt);
        return this;
    }
}