package cn.madcoder.one.module.system.service.sms;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.sms.vo.template.*;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsTemplateDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 短信模板 Service 接口
 *
 * @author mad
 * @since
 */
public interface SmsTemplateService {

    /**
     * 获得指定短信渠道下的短信模板数量
     *
     * @param channelId 短信渠道编号
     * @return 数量
     */
    Long countByChannelId(Long channelId);


    /**
     * 获得短信模板分页
     *
     * @param pageVO 分页查询
     * @return 短信模板分页
     */
    PageResult<SmsTemplateDO>  getSmsTemplatePage(SmsTemplatePageReqVO pageVO);

    /**
     * 获得短信模板
     *
     * @param id 编号
     * @return 短信模板
     */
    SmsTemplateDO getSmsTemplate(Long id);


    /**
     * 获得短信模板，从缓存中
     *
     * @param code 模板编码
     * @return 短信模板
     */
    SmsTemplateDO getSmsTemplateByCodeFromCache(String code);


    /**
     * 创建短信模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSmsTemplate(SmsTemplateCreateReqVO createReqVO);


    /**
     * 更新短信模板
     *
     * @param updateReqVO 更新信息
     */
    void updateSmsTemplate( SmsTemplateUpdateReqVO updateReqVO);

    /**
     * 删除短信模板
     *
     * @param id 编号
     */
    void deleteSmsTemplate(Long id);


    /**
     * 获得短信模板列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 短信模板分页
     */
    List<SmsTemplateDO> getSmsTemplateList(SmsTemplateExportReqVO exportReqVO);


    /**
     * 格式化短信内容
     *
     * @param content 短信模板的内容
     * @param params 内容的参数
     * @return 格式化后的内容
     */
    String formatSmsTemplateContent(String content, Map<String, Object> params);
}
