package cn.madcoder.one.module.system.dal.mysql.mail;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.framework.mybatis.core.mapper.BaseMapperX;
import cn.madcoder.one.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.madcoder.one.module.system.controller.admin.mail.vo.account.MailAccountPageReqVO;
import cn.madcoder.one.module.system.dal.dataobject.mail.MailAccountDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailAccountMapper extends BaseMapperX<MailAccountDO> {

    default PageResult<MailAccountDO> selectPage(MailAccountPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<MailAccountDO>()
                .likeIfPresent(MailAccountDO::getMail, pageReqVO.getMail())
                .likeIfPresent(MailAccountDO::getUsername , pageReqVO.getUsername()));
    }

}
