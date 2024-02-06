package cn.madcoder.one.module.infra.api.logger;

import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.madcoder.one.module.infra.dal.mysql.logger.ApiAccessLogMapper;
import cn.madcoder.one.module.infra.service.logger.ApiAccessLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;


@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ApiAccessLogApiImpl implements ApiAccessLogApi{


    @Resource
    private ApiAccessLogService apiAccessLogService;

    @Override
    public CommonResult<Boolean> createApiAccessLog(ApiAccessLogCreateReqDTO createDTO) {
        apiAccessLogService.createApiAccessLog(createDTO);
        return success(true);
    }
}
