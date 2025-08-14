package com.wechat.notice.message.builder;

import com.wechat.notice.message.enums.MessageType;
import lombok.Data;

import java.util.*;

/**
 * 图文消息构建器
 * 
 * @author fyf
 */
public class NewsMessageBuilder extends WeChatMessageBuilder.BaseMessageBuilder<NewsMessageBuilder> {
    
    private final List<Article> articles = new ArrayList<>();
    
    public NewsMessageBuilder() {
        message.setMsgType(MessageType.NEWS);
    }
    
    public NewsMessageBuilder addArticle(String title, String description, String url, String picUrl) {
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setUrl(url);
        article.setPicurl(picUrl);
        articles.add(article);
        return this;
    }
    
    public NewsMessageBuilder addArticle(Article article) {
        articles.add(article);
        return this;
    }
    
    @Override
    public com.wechat.notice.message.WeChatMessage build() {
        Map<String, Object> extra = message.getExtra();
        if (extra == null) {
            extra = new HashMap<>();
            message.setExtra(extra);
        }
        extra.put("news", Collections.singletonMap("articles", articles));
        return message;
    }
    
    /**
     * 图文消息文章
     */
    @Data
    public static class Article {
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
         * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
         */
        private String picurl;
    }
}