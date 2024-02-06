package cn.madcoder.one.framework.datasource.config;

import cn.madcoder.one.framework.datasource.core.filter.DruidAdRemoveFilter;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 数据库配置类
 *
 * @author mad
 */
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true) // 启动事务管理
@EnableConfigurationProperties(DruidStatProperties.class)
public class OneDataSourceAutoConfiguration {

    /**
     * 创建 DruidAdRemoveFilter 过滤器，过滤 common.js 的广告
     * druid监控平台
     * http://ip:端口/druid/index.html 默认无账号密码登陆，可设置 login-username:
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.web-stat-filter.enabled", havingValue = "true")
    public FilterRegistrationBean<DruidAdRemoveFilter> druidAdRemoveFilterFilter(DruidStatProperties properties) {
        // 获取 druid web 监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取 common.js 的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // 创建 DruidAdRemoveFilter Bean
        FilterRegistrationBean<DruidAdRemoveFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DruidAdRemoveFilter());
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}
