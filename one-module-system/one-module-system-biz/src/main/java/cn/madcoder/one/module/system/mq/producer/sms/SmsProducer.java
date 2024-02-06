package cn.madcoder.one.module.system.mq.producer.sms;

import cn.madcoder.one.framework.common.core.KeyValue;
import cn.madcoder.one.framework.mq.core.AbstractBusProducer;
import cn.madcoder.one.module.system.mq.message.sms.SmsSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bus.endpoint.AbstractBusEndpoint;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * Sms 短信相关消息的 Producer
 *
 * @author mad
 * @date
 */
@Slf4j
@Component
public class SmsProducer extends AbstractBusProducer {

    @Resource
    private StreamBridge streamBridge;

    /**
     * 发送 {@link SmsSendMessage} 消息
     *
     * @param logId 短信日志编号
     * @param mobile 手机号
     * @param channelId 渠道编号
     * @param apiTemplateId 短信模板编号
     * @param templateParams 短信模板参数
     */
    public void sendSmsSendMessage(Long logId, String mobile,
                                   Long channelId, String apiTemplateId, List<KeyValue<String, Object>> templateParams) {
        SmsSendMessage message = new SmsSendMessage().setLogId(logId).setMobile(mobile);
        message.setChannelId(channelId).setApiTemplateId(apiTemplateId).setTemplateParams(templateParams);
        streamBridge.send("smsSend-out-0", message);
    }
}
