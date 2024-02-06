package cn.madcoder.one.module.system.convert.sms;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.SmsTemplateCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.SmsTemplateExcelVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.SmsTemplateRespVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.SmsTemplateUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SmsTemplateConvert {

    SmsTemplateConvert INSTANCE = Mappers.getMapper(SmsTemplateConvert.class);


    PageResult<SmsTemplateRespVO> convertPage (PageResult<SmsTemplateDO> page);

    SmsTemplateRespVO convert(SmsTemplateDO bean);


    SmsTemplateDO convert(SmsTemplateCreateReqVO createReqVO);


    SmsTemplateDO convert(SmsTemplateUpdateReqVO updateReqVO);

    List<SmsTemplateExcelVO> convertList02(List<SmsTemplateDO> list);

}
