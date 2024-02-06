package cn.madcoder.one.module.system.api.permission;

import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.service.permission.PermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

@RestController
@Validated
public class PermissionApiImpl implements PermissionApi{


    @Resource
    private PermissionService permissionService;


    @Override
    public CommonResult<Boolean> hasAnyPermissions(Long userId, String... permissions) {
        boolean b = permissionService.hasAnyPermissions(userId, permissions);

        return CommonResult.success(b);
    }
}
