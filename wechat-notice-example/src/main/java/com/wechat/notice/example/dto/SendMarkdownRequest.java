package com.wechat.notice.example.dto;

import lombok.Data;

/**
 * 发送Markdown消息请求DTO
 *
 * @author fyf
 */
@Data
public class SendMarkdownRequest {
    /**
     * Markdown内容
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