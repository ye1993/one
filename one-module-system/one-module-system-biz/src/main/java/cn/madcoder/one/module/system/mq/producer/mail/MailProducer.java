package cn.madcoder.one.module.system.mq.producer.mail;

import cn.madcoder.one.framework.mq.core.AbstractBusProducer;
import cn.madcoder.one.module.system.mq.message.mail.MailSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Mail 邮件相关消息的 Producer
 *
 * @author mad
 * @since
 */
@Slf4j
@Component
public class MailProducer extends AbstractBusProducer {
    @Resource
    private StreamBridge streamBridge;

    /**
     * 发送 {@link MailSendMessage} 消息
     *
     * @param sendLogId 发送日志编码
     * @param mail 接收邮件地址
     * @param accountId 邮件账号编号
     * @param nickname 邮件发件人
     * @param title 邮件标题
     * @param content 邮件内容
     */
    public void sendMailSendMessage(Long sendLogId, String mail, Long accountId,
                                    String nickname, String title, String content) {
        MailSendMessage message = new MailSendMessage()
                .setLogId(sendLogId).setMail(mail).setAccountId(accountId)
                .setNickname(nickname).setTitle(title).setContent(content);
        streamBridge.send("mailSend-out-0", message);
    }

}
