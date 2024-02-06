package cn.madcoder.one.framework.operatelog.core.service;


import cn.hutool.core.bean.BeanUtil;
import cn.madcoder.one.module.system.api.logger.OperateLogApi;
import cn.madcoder.one.module.system.api.logger.dto.OperateLogCreateReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;


/**
 * 操作日志 Framework Service 实现类
 *
 * 基于 {@link OperateLogApi} 远程服务，记录操作日志
 *
 * @author mad
 */
@RequiredArgsConstructor
public class OperateLogFrameworkServiceImpl implements OperateLogFrameworkService{

    @Resource
    private OperateLogApi operateLogApi;

    @Override
    @Async
    public void createOperateLog(OperateLog operateLog) {
        OperateLogCreateReqDTO reqDTO = BeanUtil.copyProperties(operateLog, OperateLogCreateReqDTO.class);
        operateLogApi.createOperateLog(reqDTO);
    }
}
