package com.wechat.notice.example.dto;

import lombok.Data;

/**
 * 发送文本消息请求DTO
 *
 * @author fyf
 */
@Data
public class SendTextRequest {
    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收用户ID，多个用户用'|'分隔
     */
    private String toUser;

    /**
     * 应用名称（可选）
     */
    private String appName;
}