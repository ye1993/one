package cn.madcoder.one.module.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 项目的启动类
 *
 *
 * @author mad
 */
@SpringBootApplication
public class SystemServerApplication {

    public static void main(String[] args) {


        SpringApplication.run(SystemServerApplication.class, args);

    }

}
