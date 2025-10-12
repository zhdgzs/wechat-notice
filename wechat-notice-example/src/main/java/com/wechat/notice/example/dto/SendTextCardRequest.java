package com.wechat.notice.example.dto;

import lombok.Data;

/**
 * 发送TextCard消息请求DTO
 *
 * @author fyf
 */
@Data
public class SendTextCardRequest {
    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 链接URL
     */
    private String url;

    /**
     * 接收用户ID，多个用户用'|'分隔
     */
    private String toUser;

    /**
     * 应用名称（可选）
     */
    private String appName;
}