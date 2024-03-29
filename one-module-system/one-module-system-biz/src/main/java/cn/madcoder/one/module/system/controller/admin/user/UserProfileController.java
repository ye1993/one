package cn.madcoder.one.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.madcoder.one.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import cn.madcoder.one.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.madcoder.one.module.system.convert.user.UserConvert;
import cn.madcoder.one.module.system.dal.dataobject.dept.DeptDO;
import cn.madcoder.one.module.system.dal.dataobject.dept.PostDO;
import cn.madcoder.one.module.system.dal.dataobject.permission.RoleDO;
import cn.madcoder.one.module.system.dal.dataobject.user.AdminUserDO;
import cn.madcoder.one.module.system.service.dept.DeptService;
import cn.madcoder.one.module.system.service.dept.PostService;
import cn.madcoder.one.module.system.service.permission.PermissionService;
import cn.madcoder.one.module.system.service.permission.RoleService;
import cn.madcoder.one.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;
import static cn.madcoder.one.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;


    @GetMapping("/get")
    @Operation(summary = "获得登录用户信息")
    public CommonResult<UserProfileRespVO> profile() {
        // 获得用户基本信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        UserProfileRespVO resp = UserConvert.INSTANCE.convert03(user);
        // 获得用户角色
        List<RoleDO> userRoles = roleService.getRoleListFromCache(permissionService.getUserRoleIdListByUserId(user.getId()));
        resp.setRoles(UserConvert.INSTANCE.convertList(userRoles));
        // 获得部门信息
        if (user.getDeptId() != null) {
            DeptDO dept = deptService.getDept(user.getDeptId());
            resp.setDept(UserConvert.INSTANCE.convert02(dept));
        }
        // 获得岗位信息
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            List<PostDO> posts = postService.getPostList(user.getPostIds());
            resp.setPosts(UserConvert.INSTANCE.convertList02(posts));
        }

        return success(resp);
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }


}
