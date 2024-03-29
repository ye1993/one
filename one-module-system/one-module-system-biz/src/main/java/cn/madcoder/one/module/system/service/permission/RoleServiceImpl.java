package cn.madcoder.one.module.system.service.permission;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.madcoder.one.framework.common.enums.CommonStatusEnum;
import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.role.RoleExportReqVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import cn.madcoder.one.module.system.convert.permission.RoleConvert;
import cn.madcoder.one.module.system.dal.dataobject.permission.RoleDO;
import cn.madcoder.one.module.system.dal.mysql.permission.RoleMapper;
import cn.madcoder.one.module.system.dal.redis.RedisKeyConstants;
import cn.madcoder.one.module.system.enums.permission.DataScopeEnum;
import cn.madcoder.one.module.system.enums.permission.RoleCodeEnum;
import cn.madcoder.one.module.system.enums.permission.RoleTypeEnum;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionService permissionService;

    @Override
    public List<RoleDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }


        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<RoleDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<RoleDO> getRoleList(RoleExportReqVO reqVO) {
        return roleMapper.selectList(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();

        return ids.stream().anyMatch(id -> {
            RoleDO role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }


    @Override
    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id",
            unless = "#result == null")
    public RoleDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public Long createRole(RoleCreateReqVO reqVO, Integer type) {
        // 校验角色
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), null);
        // 插入到数据库
        RoleDO role = RoleConvert.INSTANCE.convert(reqVO);
        role.setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setDataScope(DataScopeEnum.ALL.getScope()); // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        roleMapper.insert(role);
        // 返回
        return role.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#reqVO.id")
    public void updateRole(RoleUpdateReqVO reqVO) {
        // 校验是否可以更新
        validateRoleForUpdate(reqVO.getId());
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(reqVO.getName(), reqVO.getCode(), reqVO.getId());

        // 更新到数据库
        RoleDO updateObj = RoleConvert.INSTANCE.convert(reqVO);
        roleMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void deleteRole(Long id) {
        // 校验是否可以更新
        validateRoleForUpdate(id);
        // 标记删除
        roleMapper.deleteById(id);
        // 删除相关数据
        permissionService.processRoleDeleted(id);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleStatus(Long id, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新状态
        RoleDO updateObj = new RoleDO().setId(id).setStatus(status);
        roleMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {

    }

    @Override
    public RoleDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public List<RoleDO> getRoleListByStatus(Collection<Integer> statuses) {
       return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleDO> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO reqVO) {
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleDO> roles = roleMapper.selectBatchIds(ids);
        Map<Long, RoleDO> roleMap = convertMap(roles, RoleDO::getId);
        // 校验
        ids.forEach(id -> {
            RoleDO role = roleMap.get(id);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw exception(ROLE_IS_DISABLE, role.getName());
            }
        });
    }


    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }


    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    void validateRoleForUpdate(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
