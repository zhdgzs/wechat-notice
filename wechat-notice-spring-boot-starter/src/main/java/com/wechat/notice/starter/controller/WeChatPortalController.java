package com.wechat.notice.starter.controller;

import com.wechat.notice.config.WeChatAppConfig;
import com.wechat.notice.exception.WeChatNoticeException;
import com.wechat.notice.service.WeChatAppConfigService;
import com.wechat.notice.util.CryptoUtil;
import com.wechat.notice.util.SignatureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 微信Portal认证控制器
 * 
 * @author WeChat Notice
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatPortalController {
    
    private final WeChatAppConfigService appConfigService;
    
    /**
     * 微信服务器认证回调（GET请求）
     * 
     * @param appName   应用名称
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return 解密后的随机字符串
     */
    @GetMapping("${wechat.notice.portal.path:/wx/cp/portal}/{appName}")
    public String authGet(@PathVariable String appName,
                          @RequestParam(name = "msg_signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        
        log.info("接收到微信服务器认证请求：appName={}, signature={}, timestamp={}, nonce={}, echostr={}", 
                appName, signature, timestamp, nonce, echostr);
        
        // 参数校验
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            log.error("认证参数不完整：signature={}, timestamp={}, nonce={}, echostr={}", 
                    signature, timestamp, nonce, echostr);
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        
        // 获取应用配置
        WeChatAppConfig appConfig = appConfigService.getApp(appName);
        if (appConfig == null) {
            log.error("authGet 未找到应用配置：appName={}", appName);
            throw new WeChatNoticeException(String.format("未找到对应appName=[%s]的配置，请核实！", appName));
        }
        
        // 签名验证
        if (!SignatureUtil.checkSignature(appConfig.getToken(), signature, timestamp, nonce, echostr)) {
            log.error("签名验证失败：appName={}, token={}", appName, appConfig.getToken());
            return "签名验证失败";
        }
        
        // 解密echostr
        try {
            String decryptedEchostr = CryptoUtil.decrypt(echostr, appConfig.getAesKey());
            log.info("认证成功：appName={}, decryptedEchostr={}", appName, decryptedEchostr);
            return decryptedEchostr;
        } catch (Exception e) {
            log.error("解密echostr失败：appName={}, echostr={}", appName, echostr, e);
            return "解密失败";
        }
    }
    
    /**
     * 接收微信服务器推送消息（POST请求）
     * 
     * @param appName     应用名称
     * @param requestBody 请求体
     * @param signature   微信加密签名
     * @param timestamp   时间戳
     * @param nonce       随机数
     * @return 响应结果
     */
    @PostMapping(value = "${wechat.notice.portal.path:/wx/cp/portal}/{appName}", produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appName,
                       @RequestBody String requestBody,
                       @RequestParam("msg_signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        
        log.info("\n接收微信请求：[appName=[{}], signature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                appName, signature, timestamp, nonce, requestBody);
        
        try {
            // 获取应用配置
            WeChatAppConfig appConfig = appConfigService.getApp(appName);
            if (appConfig == null) {
                log.error("post 未找到应用配置：appName={}", appName);
                throw new WeChatNoticeException(String.format("未找到对应appName=[%s]的配置，请核实！", appName));
            }
            
            // 验证签名
            if (!SignatureUtil.checkSignature(appConfig.getToken(), signature, timestamp, nonce, null)) {
                log.error("消息签名验证失败：appName={}", appName);
                return "签名验证失败";
            }
            
            // 解密消息
            try {
                String decryptedMessage = CryptoUtil.decrypt(requestBody, appConfig.getAesKey());
                log.debug("\n消息解密后内容为：\n{} ", decryptedMessage);
                
                // 这里可以添加具体的消息处理逻辑
                // 处理解密后的XML消息
                
                log.info("消息处理完成：appName={}", appName);
                return decryptedMessage;
                
            } catch (Exception decryptException) {
                log.error("解密消息失败：appName={}", appName, decryptException);
                return "解密失败";
            }
            
        } catch (Exception e) {
            log.error("处理微信消息异常：appName={}", appName, e);
            return "处理异常";
        }
    }

}