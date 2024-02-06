package cn.madcoder.one.module.system.api.user;

import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.api.user.dto.AdminUserRespDTO;
import cn.madcoder.one.module.system.convert.user.UserConvert;
import cn.madcoder.one.module.system.service.user.AdminUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;


@RestController
@Validated
public class AdminUserApiImpl implements AdminUserApi{

    @Resource
    private AdminUserService adminUserService;

    @Override
    public CommonResult<AdminUserRespDTO> getUser(Long id) {
        return success(UserConvert.INSTANCE.convert4(adminUserService.getUser(id)));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUsers(Collection<Long> ids) {
        return null;
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByDeptIds(Collection<Long> deptIds) {
        return null;
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByPostIds(Collection<Long> postIds) {
        return null;
    }

    @Override
    public CommonResult<Boolean> validateUserList(Set<Long> ids) {
        return null;
    }
}
