package cn.madcoder.one.module.system.convert.tenant;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.tenant.TenantCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.tenant.TenantExcelVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.tenant.TenantRespVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.tenant.TenantUpdateReqVO;
import cn.madcoder.one.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.tenant.TenantDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 租户 Convert
 *
 * @author mad
 */

@Mapper
public interface TenantConvert {

    TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);


    TenantDO convert(TenantCreateReqVO bean);

    TenantDO convert(TenantUpdateReqVO bean);


    TenantRespVO convert(TenantDO bean);


    PageResult<TenantRespVO> convertPage(PageResult<TenantDO> page);


    List<TenantExcelVO> convertList02(List<TenantDO> list);


    default UserCreateReqVO convert02(TenantCreateReqVO bean) {
        UserCreateReqVO reqVO = new UserCreateReqVO();
        reqVO.setUsername(bean.getUsername());
        reqVO.setPassword(bean.getPassword());
        reqVO.setNickname(bean.getContactName());
        reqVO.setMobile(bean.getContactMobile());
        return reqVO;
    }


}
