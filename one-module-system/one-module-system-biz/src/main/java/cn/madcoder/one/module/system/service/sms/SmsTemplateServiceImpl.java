package cn.madcoder.one.module.system.service.sms;


import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.madcoder.one.framework.common.enums.CommonStatusEnum;
import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.*;
import cn.madcoder.one.module.system.convert.sms.SmsTemplateConvert;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.madcoder.one.module.system.dal.mysql.sms.SmsTemplateMapper;
import cn.madcoder.one.module.system.dal.redis.RedisKeyConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.*;

@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {


    /**
     * 正则表达式，匹配 {} 中的变量
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");
    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    @Resource
    private SmsChannelService smsChannelService;

    @Override
    public Long countByChannelId(Long channelId) {
        return smsTemplateMapper.selectCountByChannelId(channelId);
    }


    @Override
    public PageResult<SmsTemplateDO> getSmsTemplatePage(SmsTemplatePageReqVO pageVO) {
        return smsTemplateMapper.selectPage(pageVO);
    }

    @Override
    public SmsTemplateDO getSmsTemplate(Long id) {
        return smsTemplateMapper.selectById(id);
    }


    @Override
    @Cacheable(cacheNames = RedisKeyConstants.SMS_TEMPLATE, key = "#code",
            unless = "#result == null")
    public SmsTemplateDO getSmsTemplateByCodeFromCache(String code) {
        return smsTemplateMapper.selectByCode(code);
    }

    @Override
    public Long createSmsTemplate(SmsTemplateCreateReqVO createReqVO) {
        // 校验短信渠道
        SmsChannelDO channelDO = validateSmsChannel(createReqVO.getChannelId());
        // 校验短信编码是否重复
        validateSmsTemplateCodeDuplicate(null, createReqVO.getCode());

        validateApiTemplate(createReqVO.getChannelId(), createReqVO.getApiTemplateId());

        // 插入
        SmsTemplateDO smsTemplateDO = SmsTemplateConvert.INSTANCE.convert(createReqVO);
        smsTemplateDO.setParams(parseTemplateContentParams(smsTemplateDO.getContent()));

        smsTemplateDO.setChannelCode(channelDO.getCode());

        smsTemplateMapper.insert(smsTemplateDO);
        return smsTemplateDO.getId();
    }


    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.SMS_TEMPLATE,
            allEntries = true)
    public void updateSmsTemplate(SmsTemplateUpdateReqVO updateReqVO) {
        // 校验存在
        validateSmsTemplateExists(updateReqVO.getId());
        // 校验短信渠道
        SmsChannelDO channelDO = validateSmsChannel(updateReqVO.getChannelId());
        // 校验短信编码是否重复
        validateSmsTemplateCodeDuplicate(updateReqVO.getId(), updateReqVO.getCode());

        SmsTemplateDO updateObj = SmsTemplateConvert.INSTANCE.convert(updateReqVO);
        updateObj.setParams(parseTemplateContentParams(updateObj.getContent()));
        updateObj.setChannelCode(channelDO.getCode());
        smsTemplateMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.SMS_TEMPLATE,
            allEntries = true) // allEntries 清空所有缓存，因为 id 不是直接的缓存 code，不好清理
    public void deleteSmsTemplate(Long id) {
        // 校验存在
        validateSmsTemplateExists(id);
        // 更新
        smsTemplateMapper.deleteById(id);
    }


    @Override
    public List<SmsTemplateDO> getSmsTemplateList(SmsTemplateExportReqVO exportReqVO) {
        return smsTemplateMapper.selectList(exportReqVO);
    }

    @Override
    public String formatSmsTemplateContent(String content, Map<String, Object> params) {
        return StrUtil.format(content, params);

    }

    public SmsChannelDO validateSmsChannel(Long channelId) {
        SmsChannelDO channelDO = smsChannelService.getSmsChannelById(channelId);
        if (channelDO == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        if (!Objects.equals(channelDO.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw exception(SMS_CHANNEL_DISABLE);
        }
        return channelDO;
    }

    public void validateSmsTemplateCodeDuplicate(Long id, String code) {
        SmsTemplateDO template = smsTemplateMapper.selectByCode(code);
        if (template == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
        if (!template.getId().equals(id)) {
            throw exception(SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验 API 短信平台的模板是否有效
     *
     * @param channelId 渠道编号
     * @param apiTemplateId API 模板编号
     */
    void validateApiTemplate(Long channelId, String apiTemplateId) {

    }


    List<String> parseTemplateContentParams(String content) {
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

    private void validateSmsTemplateExists(Long id) {
        if (smsTemplateMapper.selectById(id) == null) {
            throw exception(SMS_TEMPLATE_NOT_EXISTS);
        }
    }
}
