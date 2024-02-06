package cn.madcoder.one.module.system.enums;


//import cn.madcoder.one.framework.common.enums.RpcConstants;

import cn.madcoder.one.framework.common.enums.RpcConstants;

/**
 * API 相关的枚举
 *
 * @author
 */
public class ApiConstants {

    /**
     * 服务名
     * <p>
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String NAME = "system-server";

    //    RpcConstants.RPC_API_PREFIX +
    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/system";

    public static final String VERSION = "1.0.0";

}
