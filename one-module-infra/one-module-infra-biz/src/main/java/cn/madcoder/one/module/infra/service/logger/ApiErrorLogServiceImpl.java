package cn.madcoder.one.module.infra.service.logger;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExportReqVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.madcoder.one.module.infra.convert.logger.ApiErrorLogConvert;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.madcoder.one.module.infra.dal.mysql.logger.ApiErrorLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ApiErrorLogServiceImpl implements ApiErrorLogService{


    @Resource
    private ApiErrorLogMapper apiErrorLogMapper;

    @Override
    public PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO pageReqVO) {
        return apiErrorLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ApiErrorLogDO> getApiErrorLogList(ApiErrorLogExportReqVO exportReqVO) {
        return apiErrorLogMapper.selectList(exportReqVO);
    }

    @Override
    public void createApiErrorLog(ApiErrorLogCreateReqDTO createReqDTO) {
        ApiErrorLogDO apiErrorLogDO = ApiErrorLogConvert.INSTANCE.convert(createReqDTO);
        apiErrorLogMapper.insert(apiErrorLogDO);
    }
}
