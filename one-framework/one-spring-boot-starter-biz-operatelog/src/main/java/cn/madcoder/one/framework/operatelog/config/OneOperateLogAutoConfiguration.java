package cn.madcoder.one.framework.operatelog.config;


import cn.madcoder.one.framework.operatelog.core.aop.OperateLogAspect;
import cn.madcoder.one.framework.operatelog.core.service.OperateLog;
import cn.madcoder.one.framework.operatelog.core.service.OperateLogFrameworkService;
import cn.madcoder.one.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OneOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService() {
        return new OperateLogFrameworkServiceImpl();

    }


}
