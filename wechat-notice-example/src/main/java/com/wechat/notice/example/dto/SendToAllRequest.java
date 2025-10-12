package com.wechat.notice.example.dto;

import lombok.Data;

/**
 * 发送全员通知请求DTO
 *
 * @author fyf
 */
@Data
public class SendToAllRequest {
    /**
     * 消息内容
     */
    private String content;

    /**
     * 应用名称（可选）
     */
    private String appName;
}