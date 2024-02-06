package cn.madcoder.one.module.infra.convert.logger;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExcelVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * API 错误日志 Convert
 *
 * @author mad
 */
@Mapper
public interface ApiErrorLogConvert {

    ApiErrorLogConvert INSTANCE = Mappers.getMapper(ApiErrorLogConvert.class);


    PageResult<ApiErrorLogRespVO> convertPage(PageResult<ApiErrorLogDO> page);

    List<ApiErrorLogExcelVO> convertList(List<ApiErrorLogDO> list);


    ApiErrorLogDO convert(ApiErrorLogCreateReqDTO bean);

}
