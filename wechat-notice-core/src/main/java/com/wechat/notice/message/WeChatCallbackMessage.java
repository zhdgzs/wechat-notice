package com.wechat.notice.message;

import lombok.Data;

/**
 * 微信回调消息
 * 
 * @author fyf
 */
@Data
public class WeChatCallbackMessage {
    
    /**
     * 消息接收方的用户名
     */
    private String toUserName;
    
    /**
     * 消息发送方的用户名
     */
    private String fromUserName;
    
    /**
     * 消息创建时间（整型）
     */
    private Long createTime;
    
    /**
     * 消息类型
     */
    private String msgType;
    
    /**
     * 企业应用的id，整型
     */
    private Integer agentId;
    
    /**
     * 消息内容（文本消息）
     */
    private String content;
    
    /**
     * 消息id，64位整型
     */
    private Long msgId;
    
    /**
     * 事件类型
     */
    private String event;
    
    /**
     * 事件KEY值
     */
    private String eventKey;
    
    /**
     * 图片媒体文件id
     */
    private String mediaId;
    
    /**
     * 图片链接
     */
    private String picUrl;
    
    /**
     * 语音格式
     */
    private String format;
    
    /**
     * 语音识别结果
     */
    private String recognition;
    
    /**
     * 视频消息缩略图的媒体id
     */
    private String thumbMediaId;
    
    /**
     * 地理位置维度
     */
    private Double locationX;
    
    /**
     * 地理位置经度
     */
    private Double locationY;
    
    /**
     * 地图缩放大小
     */
    private Integer scale;
    
    /**
     * 地理位置信息
     */
    private String label;
    
    /**
     * 链接消息标题
     */
    private String title;
    
    /**
     * 链接消息描述
     */
    private String description;
    
    /**
     * 链接消息URL
     */
    private String url;
    
    /**
     * 是否为文本消息
     */
    public boolean isTextMessage() {
        return "text".equals(msgType);
    }
    
    /**
     * 是否为图片消息
     */
    public boolean isImageMessage() {
        return "image".equals(msgType);
    }
    
    /**
     * 是否为语音消息
     */
    public boolean isVoiceMessage() {
        return "voice".equals(msgType);
    }
    
    /**
     * 是否为视频消息
     */
    public boolean isVideoMessage() {
        return "video".equals(msgType);
    }
    
    /**
     * 是否为位置消息
     */
    public boolean isLocationMessage() {
        return "location".equals(msgType);
    }
    
    /**
     * 是否为链接消息
     */
    public boolean isLinkMessage() {
        return "link".equals(msgType);
    }
    
    /**
     * 是否为事件消息
     */
    public boolean isEventMessage() {
        return "event".equals(msgType);
    }
    
    /**
     * 是否为订阅事件
     */
    public boolean isSubscribeEvent() {
        return isEventMessage() && "subscribe".equals(event);
    }
    
    /**
     * 是否为取消订阅事件
     */
    public boolean isUnsubscribeEvent() {
        return isEventMessage() && "unsubscribe".equals(event);
    }
    
    /**
     * 是否为点击菜单事件
     */
    public boolean isClickEvent() {
        return isEventMessage() && "click".equals(event);
    }
}