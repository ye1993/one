package cn.madcoder.one.module.system.convert.errorcode;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.errorcode.vo.*;
import cn.madcoder.one.module.system.dal.dataobject.errorcode.ErrorCodeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ErrorCodeConvert {

    ErrorCodeConvert  INSTANCE = Mappers.getMapper(ErrorCodeConvert.class);

    ErrorCodeDO convert(ErrorCodeCreateReqVO bean);

    ErrorCodeDO convert(ErrorCodeUpdateReqVO bean);

    ErrorCodeRespVO convert(ErrorCodeDO bean);


    PageResult<ErrorCodeRespVO> convertPage(PageResult<ErrorCodeDO> pageResult);

    List<ErrorCodeExcelVO> convertList02(List<ErrorCodeDO> list);
}
