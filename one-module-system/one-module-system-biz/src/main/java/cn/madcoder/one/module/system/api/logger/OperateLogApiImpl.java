package cn.madcoder.one.module.system.api.logger;


import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.madcoder.one.module.system.service.logger.OperateLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class OperateLogApiImpl implements OperateLogApi{

    @Resource
    private OperateLogService operateLogService;

    @Override
    public CommonResult<Boolean> createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        operateLogService.createOperateLog(createReqDTO);
        return success(true);

    }
}
