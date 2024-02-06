package cn.madcoder.one.module.system.mq.consumer.sms;


import cn.madcoder.one.module.system.mq.message.sms.SmsSendMessage;
import cn.madcoder.one.module.system.service.sms.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * 针对 {@link SmsSendMessage} 的消费者
 *
 * @author mad
 */
@Component
@Slf4j
public class SmsSendConsumer implements Consumer<SmsSendMessage> {

    @Resource
    private SmsSendService smsSendService;

    @Override
    public void accept(SmsSendMessage message) {
        log.info("[accept][消息内容({})]", message);
        smsSendService.doSendSms(message);
    }
}
