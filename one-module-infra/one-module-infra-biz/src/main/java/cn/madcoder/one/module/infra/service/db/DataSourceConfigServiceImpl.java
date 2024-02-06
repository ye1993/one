package cn.madcoder.one.module.infra.service.db;

import cn.madcoder.one.framework.mybatis.core.util.JdbcUtils;
import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigCreateReqVO;
import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigUpdateReqVO;
import cn.madcoder.one.module.infra.convert.db.DataSourceConfigConvert;
import cn.madcoder.one.module.infra.dal.dataobject.db.DataSourceConfigDO;
import cn.madcoder.one.module.infra.dal.mysql.db.DataSourceConfigMapper;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static cn.madcoder.one.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.madcoder.one.module.infra.enums.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_EXISTS;
import static cn.madcoder.one.module.infra.enums.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_OK;

@Service
public class DataSourceConfigServiceImpl implements DataSourceConfigService{


    @Resource
    private DataSourceConfigMapper dataSourceConfigMapper;
    @Override
    public List<DataSourceConfigDO> getDataSourceConfigList() {
        return dataSourceConfigMapper.selectList();
    }

    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Override
    public Long createDataSourceConfig(DataSourceConfigCreateReqVO createReqVO) {
        DataSourceConfigDO dataSourceConfig = DataSourceConfigConvert.INSTANCE.convert(createReqVO);
        validateConnectionOK(dataSourceConfig);

        // 插入
        dataSourceConfigMapper.insert(dataSourceConfig);
        // 返回
        return dataSourceConfig.getId();
    }

    @Override
    public void updateDataSourceConfig(DataSourceConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateDataSourceConfigExists(updateReqVO.getId());
        DataSourceConfigDO dataSourceConfig = DataSourceConfigConvert.INSTANCE.convert(updateReqVO);
        validateConnectionOK(dataSourceConfig);
        dataSourceConfigMapper.updateById(dataSourceConfig);

    }

    @Override
    public void deleteDataSourceConfig(Long id) {
        validateDataSourceConfigExists(id);

        dataSourceConfigMapper.deleteById(id);
    }

    @Override
    public DataSourceConfigDO getDataSourceConfig(Long id) {
        // 如果 id 为 0，默认为 master 的数据源
        if (Objects.equals(id, DataSourceConfigDO.ID_MASTER)) {
            return buildMasterDataSourceConfig();
        }
        return dataSourceConfigMapper.selectOne(DataSourceConfigDO::getId,id);
    }

    private DataSourceConfigDO buildMasterDataSourceConfig() {
        String primary = dynamicDataSourceProperties.getPrimary();
        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get(primary);
        return new DataSourceConfigDO().setId(DataSourceConfigDO.ID_MASTER).setName(primary)
                .setUrl(dataSourceProperty.getUrl())
                .setUsername(dataSourceProperty.getUsername())
                .setPassword(dataSourceProperty.getPassword());
    }

    private void validateDataSourceConfigExists(Long id) {
        if (dataSourceConfigMapper.selectById(id) == null) {
            throw exception(DATA_SOURCE_CONFIG_NOT_EXISTS);
        }
    }
    private void validateConnectionOK(DataSourceConfigDO config) {
        boolean success = JdbcUtils.isConnectionOK(config.getUrl(), config.getUsername(), config.getPassword());
        if (!success) {
            throw exception(DATA_SOURCE_CONFIG_NOT_OK);
        }
    }
}
