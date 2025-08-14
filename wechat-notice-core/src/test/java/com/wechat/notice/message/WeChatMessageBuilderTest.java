package com.wechat.notice.message;

import com.wechat.notice.message.builder.WeChatMessageBuilder;
import com.wechat.notice.message.enums.MessageType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 微信消息构建器测试类
 * 验证消息构建功能可以独立运行
 * 
 * @author fyf
 */
public class WeChatMessageBuilderTest {
    
    @Test
    public void testTextMessageBuilder() {
        WeChatMessage message = WeChatMessageBuilder.text()
                .content("测试文本消息")
                .toUser("user1|user2")
                .toParty("party1")
                .toTag("tag1")
                .build();
        
        assertNotNull(message);
        assertEquals(MessageType.TEXT.getCode(), message.getMsgType());
        assertEquals("测试文本消息", message.getContent());
        assertEquals("user1|user2", message.getToUser());
        assertEquals("party1", message.getToParty());
        assertEquals("tag1", message.getToTag());
    }
    
    @Test
    public void testMarkdownMessageBuilder() {
        String markdownContent = "# 标题\n**粗体文本**\n- 列表项1\n- 列表项2";
        
        WeChatMessage message = WeChatMessageBuilder.markdown()
                .content(markdownContent)
                .toUser("@all")
                .build();
        
        assertNotNull(message);
        assertEquals(MessageType.MARKDOWN.getCode(), message.getMsgType());
        assertEquals(markdownContent, message.getContent());
        assertEquals("@all", message.getToUser());
    }
    
    @Test
    public void testTextCardMessageBuilder() {
        WeChatMessage message = WeChatMessageBuilder.textCard()
                .title("卡片标题")
                .description("卡片描述")
                .url("https://example.com")
                .btnTxt("查看详情")
                .toUser("user1")
                .build();
        
        assertNotNull(message);
        assertEquals(MessageType.TEXTCARD.getCode(), message.getMsgType());
        assertEquals("卡片标题", message.getTitle());
        assertEquals("卡片描述", message.getDescription());
        assertEquals("https://example.com", message.getUrl());
        assertEquals("查看详情", message.getBtnTxt());
        assertEquals("user1", message.getToUser());
    }
    
    @Test
    public void testNewsMessageBuilder() {
        WeChatMessage message = WeChatMessageBuilder.news()
                .addArticle("文章标题1", "文章描述1", "https://example.com/1", "https://example.com/pic1.jpg")
                .addArticle("文章标题2", "文章描述2", "https://example.com/2", "https://example.com/pic2.jpg")
                .toUser("user1")
                .build();
        
        assertNotNull(message);
        assertEquals(MessageType.NEWS.getCode(), message.getMsgType());
        assertEquals("user1", message.getToUser());
        assertNotNull(message.getExtra());
        assertTrue(message.getExtra().containsKey("articles"));
    }
    
    @Test
    public void testWeChatMessageBuilder() {
        WeChatMessage message = WeChatMessage.builder()
                .msgType(MessageType.TEXT.getCode())
                .content("直接构建的消息")
                .toUser("user1")
                .agentId(1000001)
                .build();
        
        assertNotNull(message);
        assertEquals(MessageType.TEXT.getCode(), message.getMsgType());
        assertEquals("直接构建的消息", message.getContent());
        assertEquals("user1", message.getToUser());
        assertEquals(Integer.valueOf(1000001), message.getAgentId());
    }
    
    @Test
    public void testMessageWithExtraFields() {
        WeChatMessage message = WeChatMessage.builder()
                .msgType("textcard")
                .title("卡片标题")
                .description("卡片描述")
                .url("https://example.com")
                .btnTxt("查看详情")
                .toUser("user1")
                .build();
        
        assertNotNull(message);
        assertEquals("textcard", message.getMsgType());
        assertEquals("卡片标题", message.getTitle());
        assertEquals("卡片描述", message.getDescription());
        assertEquals("https://example.com", message.getUrl());
        assertEquals("查看详情", message.getBtnTxt());
    }
    
    @Test
    public void testEmptyMessage() {
        WeChatMessage message = WeChatMessage.builder().build();
        
        assertNotNull(message);
        assertNull(message.getMsgType());
        assertNull(message.getContent());
        assertNull(message.getToUser());
    }
    
    @Test
    public void testMessageTypeEnum() {
        assertEquals("text", MessageType.TEXT.getCode());
        assertEquals("textcard", MessageType.TEXTCARD.getCode());
        assertEquals("news", MessageType.NEWS.getCode());
        assertEquals("markdown", MessageType.MARKDOWN.getCode());
        
        // 测试枚举的完整性
        assertNotNull(MessageType.valueOf("TEXT"));
        assertNotNull(MessageType.valueOf("TEXTCARD"));
        assertNotNull(MessageType.valueOf("NEWS"));
        assertNotNull(MessageType.valueOf("MARKDOWN"));
    }
}