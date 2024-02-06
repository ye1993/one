package cn.madcoder.one.module.system.dal.mysql.dept;

import cn.madcoder.one.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.madcoder.one.module.system.dal.dataobject.dept.DeptDO;

import cn.madcoder.one.framework.mybatis.core.mapper.BaseMapperX;
import cn.madcoder.one.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapperX<DeptDO> {

    default List<DeptDO> selectList(DeptListReqVO reqVO) {
        LambdaQueryWrapperX<DeptDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.likeIfPresent(DeptDO::getName, reqVO.getName());
        lambdaQueryWrapperX.eqIfPresent(DeptDO::getStatus, reqVO.getStatus());
        return selectList(lambdaQueryWrapperX);
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {


        return this.selectOne(DeptDO::getParentId,parentId,DeptDO::getName,name);
    }

    default Long selectCountByParentId(Long parentId) {

        return this.selectCount(DeptDO::getParentId, parentId);
    }

    default List<DeptDO> selectListByParentId(Collection<Long> parentIds) {

        return this.selectList(DeptDO::getParentId,parentIds);
    }

}
