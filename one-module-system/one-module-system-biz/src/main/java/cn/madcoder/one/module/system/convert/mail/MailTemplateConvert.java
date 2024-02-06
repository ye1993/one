package cn.madcoder.one.module.system.convert.mail;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.mail.vo.template.MailTemplateCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.mail.vo.template.MailTemplateRespVO;
import cn.madcoder.one.module.system.controller.admin.mail.vo.template.MailTemplateSimpleRespVO;
import cn.madcoder.one.module.system.controller.admin.mail.vo.template.MailTemplateUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.mail.MailTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MailTemplateConvert {

    MailTemplateConvert INSTANCE = Mappers.getMapper(MailTemplateConvert.class);

    MailTemplateDO convert(MailTemplateUpdateReqVO bean);

    MailTemplateDO convert(MailTemplateCreateReqVO bean);

    MailTemplateRespVO convert(MailTemplateDO bean);

    PageResult<MailTemplateRespVO> convertPage(PageResult<MailTemplateDO> pageResult);

    List<MailTemplateSimpleRespVO> convertList02(List<MailTemplateDO> list);

}
