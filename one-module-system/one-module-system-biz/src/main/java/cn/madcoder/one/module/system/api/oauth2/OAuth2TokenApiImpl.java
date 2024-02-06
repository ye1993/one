package cn.madcoder.one.module.system.api.oauth2;


import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.madcoder.one.module.system.convert.oauth2.OAuth2TokenConvert;
import cn.madcoder.one.module.system.service.oauth2.OAuth2TokenService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController  // 提供 RESTful API 接口，给 Feign 调用
public class OAuth2TokenApiImpl implements OAuth2TokenApi{

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public CommonResult<OAuth2AccessTokenCheckRespDTO> checkAccessToken(String accessToken) {
        return CommonResult.success(OAuth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken)));
    }
}
