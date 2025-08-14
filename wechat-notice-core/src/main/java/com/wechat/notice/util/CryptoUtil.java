package com.wechat.notice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.message.WeChatCallbackMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * 加解密工具类
 * 
 * @author fyf
 */
public class CryptoUtil {
    
    private static final String ALGORITHM = "AES/CBC/NoPadding";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * 4个字节的网络字节序bytes数组还原成一个数字
     */
    private static int bytesNetworkOrder2Number(byte[] bytesInNetworkOrder) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= bytesInNetworkOrder[i] & 0xff;
        }
        return sourceNumber;
    }
    
    /**
     * 删除解密后明文的补位字符（PKCS7）
     */
    private static byte[] removePKCS7Padding(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }
    
    /**
     * 解密回调数据
     *
     * @param encryptedData 加密数据
     * @param aesKey        AES密钥
     * @return 解密后的数据
     */
    public static String decrypt(String encryptedData, String aesKey) {
        try {
            // 修复：去掉不必要的"="后缀
            byte[] aesKeyBytes = Base64.decodeBase64(StringUtils.remove(aesKey, " "));
            byte[] encryptedBytes = Base64.decodeBase64(encryptedData);
            
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyBytes, 0, 16);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            // 修复：使用正确的PKCS7补位处理
            byte[] content = removePKCS7Padding(decryptedBytes);
            
            // 修复：按照正确格式解析内容
            // 格式：16字节随机字符串 + 4字节长度(网络字节序) + XML内容 + AppId
            if (content.length < 20) {
                throw new WeChatNoticeException("解密数据长度不足");
            }
            
            // 跳过前16字节随机字符串
            byte[] lengthBytes = Arrays.copyOfRange(content, 16, 20);
            int messageLength = bytesNetworkOrder2Number(lengthBytes);
            
            if (20 + messageLength > content.length) {
                throw new WeChatNoticeException("消息长度超出范围");
            }
            
            // 提取XML消息内容
            return new String(Arrays.copyOfRange(content, 20, 20 + messageLength), StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new WeChatNoticeException("消息解密失败", e);
        }
    }
    
    /**
     * 解密消息并解析为回调消息对象
     *
     * @param encryptedData 加密数据
     * @param aesKey        AES密钥
     * @param corpId        企业ID
     * @return 解析后的回调消息
     */
    public static WeChatCallbackMessage decryptMessage(String encryptedData, String aesKey, String corpId) {
        try {
            // 解密消息
            String decryptedXml = decryptMessageContent(encryptedData, aesKey, corpId);
            
            // 解析XML为Map
            Map<String, String> xmlMap = XmlUtil.parseXml(decryptedXml);
            
            // 转换为回调消息对象
            return convertMapToCallbackMessage(xmlMap);
            
        } catch (Exception e) {
            throw new WeChatNoticeException("解密并解析消息失败", e);
        }
    }
    
    /**
     * 解密消息内容
     */
    private static String decryptMessageContent(String encryptedData, String aesKey, String corpId) {
        try {
            // 修复：去掉不必要的"="后缀
            byte[] aesKeyBytes = Base64.decodeBase64(StringUtils.remove(aesKey, " "));
            byte[] encryptedBytes = Base64.decodeBase64(encryptedData);
            
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyBytes, 0, 16);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            // 修复：使用正确的PKCS7补位处理
            byte[] content = removePKCS7Padding(decryptedBytes);
            
            // 验证数据长度
            if (content.length < 20) {
                throw new WeChatNoticeException("解密数据长度不足");
            }
            
            // 解析内容：前16字节是随机字符串，接下来4字节是消息长度(网络字节序)，然后是消息内容，最后是CorpId
            // 获取消息长度（使用网络字节序）
            byte[] lengthBytes = Arrays.copyOfRange(content, 16, 20);
            int messageLength = bytesNetworkOrder2Number(lengthBytes);
            
            // 验证消息长度
            if (20 + messageLength > content.length) {
                throw new WeChatNoticeException("消息长度超出范围");
            }
            
            // 提取消息内容
            String message = new String(Arrays.copyOfRange(content, 20, 20 + messageLength), StandardCharsets.UTF_8);
            
            // 提取CorpId并验证
            int corpIdStartIndex = 20 + messageLength;
            if (corpIdStartIndex < content.length) {
                String decryptedCorpId = new String(Arrays.copyOfRange(content, corpIdStartIndex, content.length), StandardCharsets.UTF_8);
                
                if (!corpId.equals(decryptedCorpId)) {
                    throw new WeChatNoticeException("企业ID验证失败：期望" + corpId + "，实际" + decryptedCorpId);
                }
            }
            
            return message;
            
        } catch (Exception e) {
            throw new WeChatNoticeException("消息解密失败", e);
        }
    }
    
    /**
     * 将Map转换为回调消息对象
     */
    private static WeChatCallbackMessage convertMapToCallbackMessage(Map<String, String> xmlMap) {
        WeChatCallbackMessage message = new WeChatCallbackMessage();
        
        message.setToUserName(xmlMap.get("ToUserName"));
        message.setFromUserName(xmlMap.get("FromUserName"));
        
        String createTime = xmlMap.get("CreateTime");
        if (StringUtils.isNotBlank(createTime)) {
            message.setCreateTime(Long.parseLong(createTime));
        }
        
        message.setMsgType(xmlMap.get("MsgType"));
        
        String agentId = xmlMap.get("AgentID");
        if (StringUtils.isNotBlank(agentId)) {
            message.setAgentId(Integer.parseInt(agentId));
        }
        
        message.setContent(xmlMap.get("Content"));
        
        String msgId = xmlMap.get("MsgId");
        if (StringUtils.isNotBlank(msgId)) {
            message.setMsgId(Long.parseLong(msgId));
        }
        
        message.setEvent(xmlMap.get("Event"));
        message.setEventKey(xmlMap.get("EventKey"));
        message.setMediaId(xmlMap.get("MediaId"));
        message.setPicUrl(xmlMap.get("PicUrl"));
        message.setFormat(xmlMap.get("Format"));
        message.setRecognition(xmlMap.get("Recognition"));
        message.setThumbMediaId(xmlMap.get("ThumbMediaId"));
        
        String locationX = xmlMap.get("Location_X");
        if (StringUtils.isNotBlank(locationX)) {
            message.setLocationX(Double.parseDouble(locationX));
        }
        
        String locationY = xmlMap.get("Location_Y");
        if (StringUtils.isNotBlank(locationY)) {
            message.setLocationY(Double.parseDouble(locationY));
        }
        
        String scale = xmlMap.get("Scale");
        if (StringUtils.isNotBlank(scale)) {
            message.setScale(Integer.parseInt(scale));
        }
        
        message.setLabel(xmlMap.get("Label"));
        message.setTitle(xmlMap.get("Title"));
        message.setDescription(xmlMap.get("Description"));
        message.setUrl(xmlMap.get("Url"));
        
        return message;
    }
}