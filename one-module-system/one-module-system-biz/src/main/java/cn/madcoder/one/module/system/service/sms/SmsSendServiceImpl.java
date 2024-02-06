package cn.madcoder.one.module.system.service.sms;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.madcoder.one.framework.common.core.KeyValue;
import cn.madcoder.one.framework.common.enums.CommonStatusEnum;
import cn.madcoder.one.framework.common.enums.UserTypeEnum;
import cn.madcoder.one.framework.sms.core.client.SmsClient;
import cn.madcoder.one.framework.sms.core.client.SmsClientFactory;
import cn.madcoder.one.framework.sms.core.client.SmsCommonResult;
import cn.madcoder.one.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.madcoder.one.module.system.dal.dataobject.user.AdminUserDO;
import cn.madcoder.one.module.system.mq.message.sms.SmsSendMessage;
import cn.madcoder.one.module.system.mq.producer.sms.SmsProducer;
import cn.madcoder.one.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.*;


@Service
public class SmsSendServiceImpl implements SmsSendService {


    @Resource
    private SmsClientFactory smsClientFactory;

    @Resource
    private SmsChannelService smsChannelService;

    @Resource
    private SmsLogService smsLogService;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private SmsProducer smsProducer;

    @Resource
    private AdminUserService adminUserService;

    @Override
    public Long sendSingleSmsToAdmin(String mobile, Long userId, String templateCode, Map<String, Object> templateParams) {
        // 如果 mobile 为空，则加载用户编号对应的手机号
        if (StrUtil.isEmpty(mobile)) {
            AdminUserDO user = adminUserService.getUser(userId);
            if (user != null) {
                mobile = user.getMobile();
            }
        }
        return sendSingleSms(mobile, userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleSmsToMember(String mobile, Long userId, String templateCode, Map<String, Object> templateParams) {
        // 如果 mobile 为空，则加载用户编号对应的手机号
        if (StrUtil.isEmpty(mobile)) {
            AdminUserDO user = adminUserService.getUser(userId);
            if (user != null) {
                mobile = user.getMobile();
            }
        }
        return sendSingleSms(mobile, userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // 校验短信模板是否合法
        SmsTemplateDO template = validateSmsTemplate(templateCode);
        // 校验短信渠道是否合法
        SmsChannelDO smsChannel = validateSmsChannel(template.getChannelId());
        // 校验手机号码是否存在
        mobile = validateMobile(mobile);
        // 构建有序的模板参数。为什么放在这个位置，是提前保证模板参数的正确性，而不是到了插入发送日志
        List<KeyValue<String, Object>> newTemplateParams = buildTemplateParams(template, templateParams);
        // 创建发送日志。如果模板被禁用，则不发送短信，只记录日志
        Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus())
                && CommonStatusEnum.ENABLE.getStatus().equals(smsChannel.getStatus());
        ;

        String content = smsTemplateService.formatSmsTemplateContent(template.getContent(), templateParams);
        Long sendLogId = smsLogService.createSmsLog(mobile, userId, userType, isSend, template, content, templateParams);

        // 发送 MQ 消息，异步执行发送短信
        if (isSend) {
            smsProducer.sendSmsSendMessage(sendLogId, mobile, template.getChannelId(),
                    template.getApiTemplateId(), newTemplateParams);
        }

        return sendLogId;
    }


    /**
     * 将参数模板，处理成有序的 KeyValue 数组
     * <p>
     * 原因是，部分短信平台并不是使用 key 作为参数，而是数组下标，例如说腾讯云 https://cloud.tencent.com/document/product/382/39023
     *
     * @param template       短信模板
     * @param templateParams 原始参数
     * @return 处理后的参数
     */
    @VisibleForTesting
    List<KeyValue<String, Object>> buildTemplateParams(SmsTemplateDO template, Map<String, Object> templateParams) {
        return template.getParams().stream().map(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw exception(SMS_SEND_MOBILE_TEMPLATE_PARAM_MISS, key);
            }
            return new KeyValue<>(key, value);
        }).collect(Collectors.toList());
    }

    public String validateMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            throw exception(SMS_SEND_MOBILE_NOT_EXISTS);
        }
        return mobile;
    }

    SmsChannelDO validateSmsChannel(Long channelId) {
        // 获得短信模板。考虑到效率，从缓存中获取
        SmsChannelDO channelDO = smsChannelService.getSmsChannel(channelId);
        // 短信模板不存在
        if (channelDO == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        return channelDO;
    }

    SmsTemplateDO validateSmsTemplate(String templateCode) {
        // 获得短信模板。考虑到效率，从缓存中获取
        SmsTemplateDO template = smsTemplateService.getSmsTemplateByCodeFromCache(templateCode);
        // 短信模板不存在
        if (template == null) {
            throw exception(SMS_SEND_TEMPLATE_NOT_EXISTS);
        }
        return template;
    }

    @Override
    public void receiveSmsStatus(String channelCode, String text) throws Throwable {

    }


    @Override
    public void doSendSms(SmsSendMessage message) {
        // 获得渠道对应的 SmsClient 客户端
        SmsClient smsClient = smsClientFactory.getSmsClient(message.getChannelId());
        Assert.notNull(smsClient, "短信客户端({}) 不存在", message.getChannelId());

        // 发送短信
        SmsCommonResult<SmsSendRespDTO> sendResult = smsClient.sendSms(message.getLogId(), message.getMobile(),
                message.getApiTemplateId(), message.getTemplateParams());
        smsLogService.updateSmsSendResult(message.getLogId(), sendResult.getCode(), sendResult.getMsg(),
                sendResult.getApiCode(), sendResult.getApiMsg(), sendResult.getApiRequestId(),
                sendResult.getData() != null ? sendResult.getData().getSerialNo() : null);
    }
}
