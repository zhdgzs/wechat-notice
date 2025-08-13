package com.wechat.notice.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

/**
 * 签名验证工具类
 * 
 * @author WeChat Notice
 */
public class SignatureUtil {
    
    /**
     * 验证微信签名
     *
     * @param token     Token
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echoStr   回声字符串
     * @return 验证结果
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce, String echoStr) {
        String[] arr = {token, timestamp, nonce, echoStr};
        Arrays.sort(arr);
        
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        
        String sha1 = DigestUtils.sha1Hex(content.toString());
        return sha1.equals(signature);
    }
}