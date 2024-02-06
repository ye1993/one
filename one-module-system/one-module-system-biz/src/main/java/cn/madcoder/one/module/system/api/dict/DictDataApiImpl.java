package cn.madcoder.one.module.system.api.dict;

import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.api.dict.dto.DictDataRespDTO;
import cn.madcoder.one.module.system.convert.dict.DictDataConvert;
import cn.madcoder.one.module.system.dal.dataobject.dict.DictDataDO;
import cn.madcoder.one.module.system.service.dict.DictDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;


@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DictDataApiImpl implements DictDataApi{
    @Resource
    private DictDataService dictDataService;

    @Override
    public CommonResult<DictDataRespDTO> getDictData(String dictType, String value) {
        return success(DictDataConvert.INSTANCE.convert02(dictDataService.getDictData(dictType,value)));
    }


    @Override
    public CommonResult<DictDataRespDTO> parseDictData(String dictType, String label) {
        DictDataDO dictData = dictDataService.parseDictData(dictType, label);
        return success(DictDataConvert.INSTANCE.convert02(dictData));
    }
}
