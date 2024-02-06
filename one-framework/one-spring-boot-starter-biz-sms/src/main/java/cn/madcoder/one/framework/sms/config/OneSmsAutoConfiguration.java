package cn.madcoder.one.framework.sms.config;


import cn.madcoder.one.framework.sms.core.client.SmsClientFactory;
import cn.madcoder.one.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


/**
 * 短信配置类
 *
 * @author mad
 */
@AutoConfiguration
public class OneSmsAutoConfiguration {

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }
}
