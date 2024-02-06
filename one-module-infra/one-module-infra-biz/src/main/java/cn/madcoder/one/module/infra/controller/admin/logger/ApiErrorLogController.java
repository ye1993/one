package cn.madcoder.one.module.infra.controller.admin.logger;


import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.framework.excel.core.util.ExcelUtils;
import cn.madcoder.one.framework.operatelog.core.annotations.OperateLog;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExcelVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExportReqVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.madcoder.one.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import cn.madcoder.one.module.infra.convert.logger.ApiErrorLogConvert;
import cn.madcoder.one.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.madcoder.one.module.infra.service.logger.ApiErrorLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;
import static cn.madcoder.one.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - API 错误日志")
@RestController
@RequestMapping("/infra/api-error-log")
@Validated
public class ApiErrorLogController {

    @Resource
    private ApiErrorLogService apiErrorLogService;

    @GetMapping("/page")
    @Operation(summary = "获得 API 错误日志分页")
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:query')")
    public CommonResult<PageResult<ApiErrorLogRespVO>> getApiErrorLogPage(@Valid ApiErrorLogPageReqVO pageVO) {
        PageResult<ApiErrorLogDO> pageResult = apiErrorLogService.getApiErrorLogPage(pageVO);
        return success(ApiErrorLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出 API 错误日志 Excel")
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:export')")
    @OperateLog(type = EXPORT)
    public void exportApiErrorLogExcel(@Valid ApiErrorLogExportReqVO exportReqVO,
                                       HttpServletResponse response) throws IOException {
        List<ApiErrorLogDO> list = apiErrorLogService.getApiErrorLogList(exportReqVO);
        // 导出 Excel
        List<ApiErrorLogExcelVO> datas = ApiErrorLogConvert.INSTANCE.convertList(list);
        ExcelUtils.write(response, "API 错误日志.xls", "数据", ApiErrorLogExcelVO.class, datas);
    }
}
