package cn.madcoder.one.module.infra.convert.logger;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogExcelVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogRespVO;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApiAccessLogConvert {
    
    ApiAccessLogConvert INSTANCE = Mappers.getMapper(ApiAccessLogConvert.class);

    PageResult<ApiAccessLogRespVO> convertPage(PageResult<ApiAccessLogDO> page);

    List<ApiAccessLogExcelVO> convertList02(List<ApiAccessLogDO> list);


    ApiAccessLogDO convert(ApiAccessLogCreateReqDTO bean);

}
