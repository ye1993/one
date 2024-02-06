package cn.madcoder.one.module.system.service.mail;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.mail.vo.account.MailAccountCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.mail.vo.account.MailAccountPageReqVO;
import cn.madcoder.one.module.system.controller.admin.mail.vo.account.MailAccountUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.mail.MailAccountDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 邮箱账号 Service 接口
 *
 * @author mad
 * @since madcoder
 */
public interface MailAccountService {

    /**
     * 创建邮箱账号
     *
     * @param createReqVO 邮箱账号信息
     * @return 编号
     */
    Long createMailAccount(@Valid MailAccountCreateReqVO createReqVO);

    /**
     * 修改邮箱账号
     *
     * @param updateReqVO 邮箱账号信息
     */
    void updateMailAccount(@Valid MailAccountUpdateReqVO updateReqVO);

    /**
     * 删除邮箱账号
     *
     * @param id 编号
     */
    void deleteMailAccount(Long id);

    /**
     * 获取邮箱账号信息
     *
     * @param id 编号
     * @return 邮箱账号信息
     */
    MailAccountDO getMailAccount(Long id);

    /**
     * 从缓存中获取邮箱账号
     *
     * @param id 编号
     * @return 邮箱账号
     */
    MailAccountDO getMailAccountFromCache(Long id);

    /**
     * 获取邮箱账号分页信息
     *
     * @param pageReqVO 邮箱账号分页参数
     * @return 邮箱账号分页信息
     */
    PageResult<MailAccountDO> getMailAccountPage(MailAccountPageReqVO pageReqVO);

    /**
     * 获取邮箱数组信息
     *
     * @return 邮箱账号信息数组
     */
    List<MailAccountDO> getMailAccountList();

}
