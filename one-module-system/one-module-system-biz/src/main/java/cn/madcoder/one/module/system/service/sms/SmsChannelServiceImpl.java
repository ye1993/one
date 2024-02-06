package cn.madcoder.one.module.system.service.sms;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.framework.sms.core.client.SmsClientFactory;
import cn.madcoder.one.framework.sms.core.property.SmsChannelProperties;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import cn.madcoder.one.module.system.convert.sms.SmsChannelConvert;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.madcoder.one.module.system.dal.mysql.sms.SmsChannelMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.SMS_CHANNEL_HAS_CHILDREN;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.SMS_CHANNEL_NOT_EXISTS;


@Slf4j
@Service
public class SmsChannelServiceImpl implements SmsChannelService {

    /**
     * 短信渠道列表的缓存
     */
    @Getter
    private volatile List<SmsChannelDO> channelCache = Collections.emptyList();

    @Resource
    private SmsChannelMapper smsChannelMapper;


    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private SmsClientFactory smsClientFactory;

    @Override
    public PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO pageReqVO) {
        return smsChannelMapper.selectPage(pageReqVO);
    }


    @Override
    public List<SmsChannelDO> getSmsChannelList() {
        return smsChannelMapper.selectList();
    }

    @Override
    public SmsChannelDO getSmsChannelById(Long id) {
        return smsChannelMapper.selectById(id);
    }

    @Override
    public Long createSmsChannel(SmsChannelCreateReqVO reqVO) {

        SmsChannelDO smsChannelDO = SmsChannelConvert.INSTANCE.convert(reqVO);
        smsChannelMapper.insert(smsChannelDO);
        // 刷新缓存
        initLocalCache();
        return smsChannelDO.getId();
    }

    @Override
    public void updateSmsChannel(SmsChannelUpdateReqVO reqVO) {
        // 校验存在
        validateSmsChannelExists(reqVO.getId());

        SmsChannelDO smsChannelDO = SmsChannelConvert.INSTANCE.convert(reqVO);
        smsChannelMapper.updateById(smsChannelDO);
        // 刷新缓存
        initLocalCache();
    }

    @Override
    public void deleteSmsChannel(Long id) {
        // 校验存在
        validateSmsChannelExists(id);
        // 校验是否有在使用该账号的模版

        if (smsTemplateService.countByChannelId(id) > 0) {
            throw exception(SMS_CHANNEL_HAS_CHILDREN);
        }
        // 删除
        smsChannelMapper.deleteById(id);

        // 刷新缓存
        initLocalCache();
    }

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<SmsChannelDO> channels = smsChannelMapper.selectList();
        log.info("[initLocalCache][缓存短信渠道，数量为:{}]", channels.size());

        // 第二步：构建缓存：创建或更新短信 Client
        List<SmsChannelProperties> propertiesList = SmsChannelConvert.INSTANCE.convertList02(channels);
        propertiesList.forEach(properties -> smsClientFactory.createOrUpdateSmsClient(properties));
        this.channelCache = channels;
    }

    @Override
    public SmsChannelDO getSmsChannel(Long id) {
        return smsChannelMapper.selectById(id);
    }

    private void validateSmsChannelExists(Long id) {
        if (smsChannelMapper.selectById(id) == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
    }
}
