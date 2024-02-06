package cn.madcoder.one.module.system.controller.admin.auth;


import cn.hutool.core.util.StrUtil;
import cn.madcoder.one.framework.common.enums.CommonStatusEnum;
import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.framework.operatelog.core.annotations.OperateLog;
import cn.madcoder.one.framework.security.config.SecurityProperties;
import cn.madcoder.one.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.madcoder.one.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.madcoder.one.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import cn.madcoder.one.module.system.convert.auth.AuthConvert;
import cn.madcoder.one.module.system.dal.dataobject.permission.MenuDO;
import cn.madcoder.one.module.system.dal.dataobject.permission.RoleDO;
import cn.madcoder.one.module.system.dal.dataobject.user.AdminUserDO;
import cn.madcoder.one.module.system.enums.logger.LoginLogTypeEnum;
import cn.madcoder.one.module.system.service.auth.AdminAuthService;
import cn.madcoder.one.module.system.service.permission.MenuService;
import cn.madcoder.one.module.system.service.permission.PermissionService;
import cn.madcoder.one.module.system.service.permission.RoleService;
import cn.madcoder.one.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Set;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;
import static cn.madcoder.one.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.madcoder.one.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;
import static cn.madcoder.one.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 认证")
@RestController
@RequestMapping("/system/auth")
@Validated
@Slf4j
public class AuthController {


    @Resource
    private AdminAuthService authService;


    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private AdminUserService userService;

    @Resource
    private RoleService roleService;


    @Resource
    private PermissionService permissionService;


    @Resource
    private MenuService menuService;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = obtainAuthorization(request, securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }


    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {

        return success(authService.refreshToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo(){
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        if (user == null) {
            return null;
        }

        // 1.2 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(user.getId());
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色


        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, RoleDO::getId));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus())); // 移除禁用的菜单

        return success(AuthConvert.INSTANCE.convert(user,roles,menuList));

    }
}

