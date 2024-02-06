package cn.madcoder.one.framework.security.config;

import cn.madcoder.one.framework.security.core.rpc.LoginUserRequestInterceptor;
import cn.madcoder.one.module.system.api.oauth2.OAuth2TokenApi;
import cn.madcoder.one.module.system.api.permission.PermissionApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableFeignClients(clients = {PermissionApi.class, OAuth2TokenApi.class})
public class OneSecurityRpcAutoConfiguration {

    @Bean
    public LoginUserRequestInterceptor loginUserRequestInterceptor() {
        return new LoginUserRequestInterceptor();
    }
}
