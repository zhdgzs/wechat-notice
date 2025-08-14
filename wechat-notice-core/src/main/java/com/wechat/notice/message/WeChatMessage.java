package com.wechat.notice.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wechat.notice.message.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 微信消息实体
 * 
 * @author fyf
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatMessage {
    
    /**
     * 指定接收消息的成员，成员ID列表（多个接收者用'|'分隔，最多支持1000个）
     * 特殊情况：指定为"@all"，则向该企业应用的全部成员发送
     */
    @JsonProperty("touser")
    private String toUser;
    
    /**
     * 指定接收消息的部门，部门ID列表，多个接收者用'|'分隔，最多支持100个
     * 当touser为"@all"时忽略本参数
     */
    @JsonProperty("toparty")
    private String toParty;
    
    /**
     * 指定接收消息的标签，标签ID列表，多个接收者用'|'分隔，最多支持100个
     * 当touser为"@all"时忽略本参数
     */
    @JsonProperty("totag")
    private String toTag;
    
    /**
     * 消息类型
     */
    @JsonProperty("msgtype")
    private String msgType;
    
    /**
     * 企业应用的id，整型
     */
    @JsonProperty("agentid")
    private Integer agentId;
    
    /**
     * 消息内容，最长不超过2048个字节，超过将截断（支持id转译）
     */
    private String content;
    
    /**
     * 媒体文件id，可以调用上传临时素材接口获取
     */
    @JsonProperty("media_id")
    private String mediaId;
    
    /**
     * 标题，不超过128个字节，超过会自动截断（支持id转译）
     */
    private String title;
    
    /**
     * 描述，不超过512个字节，超过会自动截断（支持id转译）
     */
    private String description;
    
    /**
     * 点击后跳转的链接
     */
    private String url;
    
    /**
     * 按钮文字，默认为"详情"，不超过4个文字，超过自动截断
     */
    @JsonProperty("btntxt")
    private String btnTxt;
    
    /**
     * 其他扩展字段，用于复杂消息类型
     */
    private Map<String, Object> extra;
    
    /**
     * 设置消息类型
     */
    public void setMsgType(MessageType messageType) {
        this.msgType = messageType.getCode();
    }
    
    /**
     * 获取消息类型枚举
     */
    public MessageType getMsgTypeEnum() {
        for (MessageType type : MessageType.values()) {
            if (type.getCode().equals(this.msgType)) {
                return type;
            }
        }
        return null;
    }
}