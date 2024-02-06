package cn.madcoder.one.module.system.convert.oauth2;


import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.madcoder.one.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenRespVO;
import cn.madcoder.one.module.system.convert.dept.DeptConvert;
import cn.madcoder.one.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OAuth2TokenConvert {


    OAuth2TokenConvert INSTANCE = Mappers.getMapper(OAuth2TokenConvert.class);

    OAuth2AccessTokenCheckRespDTO convert(OAuth2AccessTokenDO bean);

    PageResult<OAuth2AccessTokenRespVO> convert(PageResult<OAuth2AccessTokenDO> page);

}
