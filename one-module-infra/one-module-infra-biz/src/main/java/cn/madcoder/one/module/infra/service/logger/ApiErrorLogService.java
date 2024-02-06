package cn.madcoder.one.module.infra.service.logger;



import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExportReqVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiErrorLogDO;

import java.util.List;

/**
 * API 错误日志 Service 接口
 *
 * @author mad
 */
public interface ApiErrorLogService {



    /**
     * 获得 API 错误日志分页
     *
     * @param pageReqVO 分页查询
     * @return API 错误日志分页
     */
    PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO pageReqVO);

    /**
     * 获得 API 错误日志列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return API 错误日志分页
     */
    List<ApiErrorLogDO> getApiErrorLogList(ApiErrorLogExportReqVO exportReqVO);


    /**
     * 创建 API 错误日志
     *
     * @param createReqDTO API 错误日志
     */
    void createApiErrorLog(ApiErrorLogCreateReqDTO createReqDTO);

}
