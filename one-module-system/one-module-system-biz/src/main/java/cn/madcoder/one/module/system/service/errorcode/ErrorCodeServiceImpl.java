package cn.madcoder.one.module.system.service.errorcode;

import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.module.system.controller.admin.errorcode.vo.ErrorCodeCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.errorcode.vo.ErrorCodeExportReqVO;
import cn.madcoder.one.module.system.controller.admin.errorcode.vo.ErrorCodePageReqVO;
import cn.madcoder.one.module.system.controller.admin.errorcode.vo.ErrorCodeUpdateReqVO;
import cn.madcoder.one.module.system.convert.errorcode.ErrorCodeConvert;
import cn.madcoder.one.module.system.dal.dataobject.errorcode.ErrorCodeDO;
import cn.madcoder.one.module.system.dal.mysql.errorcode.ErrorCodeMapper;
import cn.madcoder.one.module.system.enums.errorcode.ErrorCodeTypeEnum;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.ERROR_CODE_DUPLICATE;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.ERROR_CODE_NOT_EXISTS;

@Service
public class ErrorCodeServiceImpl implements ErrorCodeService{


    @Resource
    private ErrorCodeMapper errorCodeMapper;

    @Override
    public Long createErrorCode(ErrorCodeCreateReqVO createReqVO) {
        // 校验 code 重复
        validateCodeDuplicate(createReqVO.getCode(), null);

        // 插入
        ErrorCodeDO errorCodeDO = ErrorCodeConvert.INSTANCE.convert(createReqVO);
        errorCodeMapper.insert(errorCodeDO);
        return errorCodeDO.getId();
    }

    @Override
    public void updateErrorCode(ErrorCodeUpdateReqVO reqVO) {
        // 校验存在
        validateErrorCodeExists(reqVO.getId());
        // 校验 code 重复
        validateCodeDuplicate(reqVO.getCode(), reqVO.getId());
        ErrorCodeDO updateErrorDode = ErrorCodeConvert.INSTANCE.convert(reqVO)
                .setType(ErrorCodeTypeEnum.MANUAL_OPERATION.getType());
        errorCodeMapper.updateById(updateErrorDode);


    }


    @Override
    public void deleteErrorCode(Long id) {
        // 校验存在
        validateErrorCodeExists(id);

        errorCodeMapper.deleteById(id);
    }


    @Override
    public ErrorCodeDO getErrorCode(Long id) {
        return errorCodeMapper.selectById(id);
    }

    @Override
    public PageResult<ErrorCodeDO> getErrorCodePage(ErrorCodePageReqVO pageReqVO) {
        return errorCodeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ErrorCodeDO> getErrorCodeList(ErrorCodeExportReqVO exportReqVO) {
        return errorCodeMapper.selectList(exportReqVO);
    }

    /**
     * 校验错误码的唯一字段是否重复
     *
     * 是否存在相同编码的错误码
     *
     * @param code 错误码编码
     * @param id 错误码编号
     */
    public void validateCodeDuplicate(Integer code, Long id) {
        ErrorCodeDO errorCodeDO = errorCodeMapper.selectByCode(code);
        if (errorCodeDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的错误码
        if (id == null) {
            throw exception(ERROR_CODE_DUPLICATE);
        }
        if (!errorCodeDO.getId().equals(id)) {
            throw exception(ERROR_CODE_DUPLICATE);
        }
    }

    void validateErrorCodeExists(Long id) {
        if (errorCodeMapper.selectById(id) == null) {
            throw exception(ERROR_CODE_NOT_EXISTS);
        }
    }
}
