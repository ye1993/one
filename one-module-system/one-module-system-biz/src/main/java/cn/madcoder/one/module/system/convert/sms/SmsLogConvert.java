package cn.madcoder.one.module.system.convert.sms;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.sms.vo.log.SmsLogExcelVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.log.SmsLogRespVO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信日志 Convert
 *
 * @author mad
 */
@Mapper
public interface SmsLogConvert {


    SmsLogConvert INSTANCE = Mappers.getMapper(SmsLogConvert.class);


    PageResult<SmsLogRespVO> convertPage(PageResult<SmsLogDO> pageResult);

    List<SmsLogExcelVO> convertList02(List<SmsLogDO> list);


}
