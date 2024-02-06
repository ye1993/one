package cn.madcoder.one.module.system.service.sms;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.madcoder.one.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.sms.SmsChannelDO;

import javax.validation.Valid;
import java.util.List;

public interface SmsChannelService {


    /**
     * 获得短信渠道分页
     *
     * @param pageReqVO 分页查询
     * @return 短信渠道分页
     */
    PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO pageReqVO);


    /**
     * 获得所有短信渠道列表
     *
     * @return 短信渠道列表
     */
    List<SmsChannelDO> getSmsChannelList();
    /**
     * 获得所有短信渠道
     *
     * @return 短信渠道
     */
    SmsChannelDO getSmsChannelById(Long id);

    /**
     * 创建短信渠道
     *
     * @param reqVO 创建信息
     * @return 编号
     */
    Long createSmsChannel(@Valid SmsChannelCreateReqVO reqVO);


    /**
     * 删除短信渠道
     *
     * @param id 编号
     */
    void deleteSmsChannel(Long id);


    /**
     * 创建短信渠道
     *
     * @param reqVO 创建信息
     * @return 编号
     */
    void updateSmsChannel(@Valid SmsChannelUpdateReqVO reqVO);
    /**
     * 初始化短信客户端
     */
    void initLocalCache();


    /**
     * 获得短信渠道
     *
     * @param id 编号
     * @return 短信渠道
     */
    SmsChannelDO getSmsChannel(Long id);
}
