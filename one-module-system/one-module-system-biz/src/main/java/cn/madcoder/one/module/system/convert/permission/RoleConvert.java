package cn.madcoder.one.module.system.convert.permission;


import cn.madcoder.one.module.system.controller.admin.permission.vo.role.*;
import cn.madcoder.one.module.system.dal.dataobject.permission.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);


    RoleRespVO convert(RoleDO role);

    RoleDO convert(RoleCreateReqVO bean);

    RoleDO convert(RoleUpdateReqVO role);



    List<RoleSimpleRespVO> convertList02(List<RoleDO> list);

    List<RoleExcelVO> convertList03(List<RoleDO> list);


}
