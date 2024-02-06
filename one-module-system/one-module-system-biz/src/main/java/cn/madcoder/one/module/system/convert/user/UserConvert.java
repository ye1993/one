package cn.madcoder.one.module.system.convert.user;


import cn.madcoder.one.module.system.api.user.dto.AdminUserRespDTO;
import cn.madcoder.one.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.madcoder.one.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.madcoder.one.module.system.controller.admin.user.vo.user.*;
import cn.madcoder.one.module.system.dal.dataobject.dept.DeptDO;
import cn.madcoder.one.module.system.dal.dataobject.dept.PostDO;
import cn.madcoder.one.module.system.dal.dataobject.permission.RoleDO;
import cn.madcoder.one.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);


    UserPageItemRespVO convert(AdminUserDO bean);


    AdminUserDO convert(UserCreateReqVO bean);


    UserPageItemRespVO.Dept convert(DeptDO bean);

    AdminUserDO convert(UserUpdateReqVO bean);

    AdminUserDO convert(UserProfileUpdateReqVO bean);

    AdminUserDO convert(UserImportExcelVO bean);



    UserProfileRespVO.Dept convert02(DeptDO bean);

    UserExcelVO convert02(AdminUserDO bean);




    List<UserProfileRespVO.Post> convertList02(List<PostDO> list);




    UserProfileRespVO convert03(AdminUserDO bean);

    List<UserProfileRespVO.Role> convertList(List<RoleDO> list);

    AdminUserRespDTO convert4(AdminUserDO bean);


    List<UserSimpleRespVO> convertList04(List<AdminUserDO> list);



}
