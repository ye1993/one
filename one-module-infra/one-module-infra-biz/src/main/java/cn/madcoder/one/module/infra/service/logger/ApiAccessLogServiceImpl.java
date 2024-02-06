package cn.madcoder.one.module.infra.service.logger;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogExportReqVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.madcoder.one.module.infra.convert.logger.ApiAccessLogConvert;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import cn.madcoder.one.module.infra.dal.mysql.logger.ApiAccessLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ApiAccessLogServiceImpl implements ApiAccessLogService{


    @Resource
    private ApiAccessLogMapper apiAccessLogMapper;

    @Override
    public PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO) {
        return apiAccessLogMapper.selectPage(pageReqVO);
    }


    @Override
    public List<ApiAccessLogDO> getApiAccessLogList(ApiAccessLogExportReqVO exportReqVO) {
        return apiAccessLogMapper.selectList(exportReqVO);

    }


    @Override
    public void createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO) {
        ApiAccessLogDO apiAccessLog = ApiAccessLogConvert.INSTANCE.convert(createReqDTO);
        
        apiAccessLogMapper.insert(apiAccessLog);

    }
}
