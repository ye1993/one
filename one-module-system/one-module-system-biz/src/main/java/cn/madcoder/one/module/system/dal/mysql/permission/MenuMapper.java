package cn.madcoder.one.module.system.dal.mysql.permission;

import cn.madcoder.one.framework.mybatis.core.mapper.BaseMapperX;
import cn.madcoder.one.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.madcoder.one.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.madcoder.one.module.system.dal.dataobject.permission.MenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapperX<MenuDO> {

    default List<MenuDO> selectListByPermission(String permission) {
        return selectList(MenuDO::getPermission, permission);
    }


    default MenuDO selectByParentIdAndName(Long parentId,String name) {
        return selectOne(MenuDO::getParentId,parentId,MenuDO::getName,name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(MenuDO::getParentId,parentId);
    }


    default List<MenuDO> selectList(MenuListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MenuDO>()
                .likeIfPresent(MenuDO::getName, reqVO.getName())
                .eqIfPresent(MenuDO::getStatus, reqVO.getStatus()));
    }
}
