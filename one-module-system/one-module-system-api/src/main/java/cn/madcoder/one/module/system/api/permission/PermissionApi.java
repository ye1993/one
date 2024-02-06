package cn.madcoder.one.module.system.api.permission;


import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;

@FeignClient(name = ApiConstants.NAME) // TODO ：fallbackFactory =  如果不用注册中心这里加上url实际服务地址 url = “http://localhost:38081/”
@Tag(name = "RPC 服务 - 权限")
public interface PermissionApi {


    String PREFIX = ApiConstants.PREFIX + "/permission";


    @GetMapping(PREFIX + "/has-any-permissions")
    @Operation(summary = "判断是否有权限，任一一个即可")
    @Parameters({
            @Parameter(name = "userId", description = "用户编号", example = "1", required = true),
            @Parameter(name = "permissions", description = "权限", example = "read,write", required = true)
    })
    CommonResult<Boolean> hasAnyPermissions(@RequestParam("userId") Long userId,
                                            @RequestParam("permissions") String... permissions);


}
