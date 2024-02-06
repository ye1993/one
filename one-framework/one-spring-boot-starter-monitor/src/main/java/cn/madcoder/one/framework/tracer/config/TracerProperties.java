package cn.madcoder.one.framework.tracer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer配置类
 *
 * @author mad
 */
@ConfigurationProperties("one.tracer")
@Data
public class TracerProperties {
}
