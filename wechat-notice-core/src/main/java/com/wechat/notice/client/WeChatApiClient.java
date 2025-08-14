package com.wechat.notice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.config.WeChatNoticeProperties;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.message.WeChatApiResponse;
import com.wechat.notice.message.WeChatMessage;
import com.wechat.notice.message.WeChatMessageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信API客户端
 * 
 * @author fyf
 */
@Slf4j
@RequiredArgsConstructor
public class WeChatApiClient {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final WeChatTokenManager tokenManager;
    private final WeChatNoticeProperties properties;
    
    /**
     * 获取访问令牌
     */
    public String getAccessToken(WeChatAppConfig appConfig) {
        return tokenManager.getAccessToken(appConfig);
    }
    
    /**
     * 发送消息
     */
    public WeChatMessageResult sendMessage(String accessToken, WeChatMessage message) {
        String baseUrl = properties.getApi().getBaseUrl();
        String url = String.format("%s/cgi-bin/message/send?access_token=%s", baseUrl, accessToken);
        
        try {
            // 构建请求体
            Map<String, Object> requestBody = buildMessageRequestBody(message);
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            
            log.debug("发送消息请求: {}", requestBodyJson);
            
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json; charset=UTF-8");
            post.setHeader("User-Agent", "WeChat-Notice-Client/1.0.0");
            post.setEntity(new StringEntity(requestBodyJson, StandardCharsets.UTF_8));
            
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.debug("发送消息响应: {}", responseBody);
                
                WeChatApiResponse apiResponse = objectMapper.readValue(responseBody, WeChatApiResponse.class);
                
                if (apiResponse.getErrcode() != null && apiResponse.getErrcode() != 0) {
                    return WeChatMessageResult.failure(apiResponse.getErrcode(), apiResponse.getErrmsg());
                }
                
                return WeChatMessageResult.success(apiResponse.getMsgid());
            }
        } catch (IOException e) {
            log.error("发送微信消息HTTP请求失败", e);
            throw new WeChatNoticeException("发送微信消息HTTP请求失败", e);
        }
    }
    
    /**
     * 构建消息请求体
     */
    private Map<String, Object> buildMessageRequestBody(WeChatMessage message) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // 基础字段
        requestBody.put("touser", message.getToUser());
        requestBody.put("toparty", message.getToParty());
        requestBody.put("totag", message.getToTag());
        requestBody.put("msgtype", message.getMsgType());
        requestBody.put("agentid", message.getAgentId());
        
        // 根据消息类型添加特定字段
        String msgType = message.getMsgType();
        if ("text".equals(msgType)) {
            Map<String, String> text = new HashMap<>();
            text.put("content", message.getContent());
            requestBody.put("text", text);
        } else if ("image".equals(msgType)) {
            Map<String, String> image = new HashMap<>();
            image.put("media_id", message.getMediaId());
            requestBody.put("image", image);
        } else if ("markdown".equals(msgType)) {
            Map<String, String> markdown = new HashMap<>();
            markdown.put("content", message.getContent());
            requestBody.put("markdown", markdown);
        } else if ("textcard".equals(msgType)) {
            Map<String, String> textcard = new HashMap<>();
            textcard.put("title", message.getTitle());
            textcard.put("description", message.getDescription());
            textcard.put("url", message.getUrl());
            textcard.put("btntxt", message.getBtnTxt());
            requestBody.put("textcard", textcard);
        }
        
        // 处理扩展字段
        if (message.getExtra() != null && !message.getExtra().isEmpty()) {
            requestBody.putAll(message.getExtra());
        }
        
        return requestBody;
    }
}