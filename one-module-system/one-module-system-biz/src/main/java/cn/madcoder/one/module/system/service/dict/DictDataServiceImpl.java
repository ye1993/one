package cn.madcoder.one.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import cn.madcoder.one.framework.common.enums.CommonStatusEnum;
import cn.madcoder.one.framework.common.pojo.PageResult;
import cn.madcoder.one.framework.common.util.collection.CollectionUtils;
import cn.madcoder.one.module.system.controller.admin.dict.vo.data.DictDataCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.dict.vo.data.DictDataExportReqVO;
import cn.madcoder.one.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import cn.madcoder.one.module.system.controller.admin.dict.vo.data.DictDataUpdateReqVO;
import cn.madcoder.one.module.system.convert.dict.DictDataConvert;
import cn.madcoder.one.module.system.dal.dataobject.dict.DictDataDO;
import cn.madcoder.one.module.system.dal.dataobject.dict.DictTypeDO;
import cn.madcoder.one.module.system.dal.mysql.dict.DictDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.system.enums.ErrorCodeConstants.*;


/**
 * 字典数据 Service 实现类
 *
 * @author mad
 */
@Service
@Slf4j
public class DictDataServiceImpl implements DictDataService{


    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictDataDO::getDictType)
            .thenComparingInt(DictDataDO::getSort);

    @Resource
    private DictDataMapper dictDataMapper;


    @Resource
    private DictTypeService dictTypeService;

    @Override
    public Long createDictData(DictDataCreateReqVO reqVO) {
        // 校验正确性
        validateDictDataForCreateOrUpdate(null, reqVO.getValue(), reqVO.getDictType());
        // 插入字典类型
        DictDataDO dictData = DictDataConvert.INSTANCE.convert(reqVO);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataUpdateReqVO reqVO) {
        // 校验正确性
        validateDictDataForCreateOrUpdate(reqVO.getId(), reqVO.getValue(), reqVO.getDictType());

        // 更新字典类型
        DictDataDO updateObj = DictDataConvert.INSTANCE.convert(reqVO);
        dictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    @Override
    public List<DictDataDO> getDictDataList() {
        List<DictDataDO> list = dictDataMapper.selectList();
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }


    @Override
    public List<DictDataDO> getDictDataList(DictDataExportReqVO reqVO) {
        List<DictDataDO> list = dictDataMapper.selectList(reqVO);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO reqVO) {
        return dictDataMapper.selectPage(reqVO);
    }

    @Override
    public DictDataDO getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public long countByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, DictDataDO> dictDataMap = CollectionUtils.convertMap(
                dictDataMapper.selectByDictTypeAndValues(dictType, values), DictDataDO::getValue);

    }

    @Override
    public DictDataDO getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public DictDataDO parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    private void validateDictDataForCreateOrUpdate(Long id, String value, String dictType) {
        // 校验自己存在
        validateDictDataExists(id);
        // 校验字典类型有效
        validateDictTypeExists(dictType);
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(id, dictType, value);
    }


    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        DictDataDO dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
    }

    public void validateDictTypeExists(String type) {
        DictTypeDO dictType = dictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
    }
}
