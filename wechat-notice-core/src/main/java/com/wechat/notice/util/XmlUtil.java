package com.wechat.notice.util;

import com.wechat.notice.exception.WeChatNoticeException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML解析工具类
 * 
 * @author fyf
 */
public class XmlUtil {
    
    private static final Pattern CDATA_PATTERN = Pattern.compile("<([^>]*)><!\\[CDATA\\[([^\\]]*?)\\]\\]></[^>]*>");
    private static final Pattern ELEMENT_PATTERN = Pattern.compile("<([^/>]+)>([^<]*)</[^>]*>");
    
    /**
     * 解析XML字符串为Map
     * 
     * @param xml XML字符串
     * @return 解析后的Map
     */
    public static Map<String, String> parseXml(String xml) {
        if (StringUtils.isBlank(xml)) {
            throw new WeChatNoticeException("XML内容不能为空");
        }
        
        Map<String, String> result = new HashMap<>();
        
        try {
            // 处理CDATA节点
            Matcher cdataMatcher = CDATA_PATTERN.matcher(xml);
            while (cdataMatcher.find()) {
                String key = cdataMatcher.group(1);
                String value = cdataMatcher.group(2);
                result.put(key, value);
            }
            
            // 处理普通文本节点
            Matcher elementMatcher = ELEMENT_PATTERN.matcher(xml);
            while (elementMatcher.find()) {
                String key = elementMatcher.group(1);
                String value = elementMatcher.group(2);
                // 如果CDATA中没有这个key，才添加
                if (!result.containsKey(key)) {
                    result.put(key, value);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            throw new WeChatNoticeException("XML解析失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从XML中提取指定字段的值
     * 
     * @param xml XML字符串
     * @param fieldName 字段名
     * @return 字段值
     */
    public static String extractField(String xml, String fieldName) {
        Map<String, String> fields = parseXml(xml);
        return fields.get(fieldName);
    }
    
    /**
     * 生成XML响应
     * 
     * @param content 响应内容
     * @return XML字符串
     */
    public static String buildXmlResponse(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        return String.format("<xml><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>", content);
    }
    
    /**
     * 构建加密响应XML
     * 
     * @param encrypt 加密内容
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 加密响应XML
     */
    public static String buildEncryptedResponse(String encrypt, String signature, String timestamp, String nonce) {
        return String.format(
            "<xml><Encrypt><![CDATA[%s]]></Encrypt><MsgSignature><![CDATA[%s]]></MsgSignature><TimeStamp>%s</TimeStamp><Nonce><![CDATA[%s]]></Nonce></xml>",
            encrypt, signature, timestamp, nonce
        );
    }
}