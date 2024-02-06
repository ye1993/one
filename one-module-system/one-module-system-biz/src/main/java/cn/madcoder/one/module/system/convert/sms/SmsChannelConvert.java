package cn.madcoder.one.module.system.convert.sms;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.framework.sms.core.property.SmsChannelProperties;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelRespVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelSimpleRespVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsChannelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SmsChannelConvert {

    SmsChannelConvert INSTANCE = Mappers.getMapper(SmsChannelConvert.class);

    PageResult<SmsChannelRespVO> convertPage(PageResult<SmsChannelDO> page);

    SmsChannelRespVO convert(SmsChannelDO bean);

    SmsChannelDO convert(SmsChannelUpdateReqVO bean);

    SmsChannelDO convert(SmsChannelCreateReqVO bean);

    List<SmsChannelProperties> convertList02(List<SmsChannelDO> list);

    List<SmsChannelSimpleRespVO> convertList03(List<SmsChannelDO> list);


}
