package cn.madcoder.one.module.system.convert.tenant;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.packages.TenantPackageCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.packages.TenantPackageRespVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.packages.TenantPackageSimpleRespVO;
import cn.madcoder.one.module.system.controller.admin.tenant.vo.packages.TenantPackageUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.tenant.TenantPackageDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 租户套餐 Convert
 *
 * @author mad
 */
@Mapper
public interface TenantPackageConvert {


    TenantPackageConvert INSTANCE = Mappers.getMapper(TenantPackageConvert.class);

    TenantPackageDO convert(TenantPackageCreateReqVO bean);

    TenantPackageDO convert(TenantPackageUpdateReqVO bean);

    TenantPackageRespVO convert(TenantPackageDO bean);

    PageResult<TenantPackageRespVO> convertPage(PageResult<TenantPackageDO> page);

    List<TenantPackageSimpleRespVO> convertList02(List<TenantPackageDO> list);



}
