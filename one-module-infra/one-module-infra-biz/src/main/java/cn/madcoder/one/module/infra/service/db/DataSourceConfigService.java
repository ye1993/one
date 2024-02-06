package cn.madcoder.one.module.infra.service.db;

import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigCreateReqVO;
import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigUpdateReqVO;
import cn.madcoder.one.module.infra.dal.dataobject.db.DataSourceConfigDO;

import javax.validation.Valid;
import java.util.List;

public interface DataSourceConfigService {


    List<DataSourceConfigDO>  getDataSourceConfigList();


    /**
     * 创建数据源配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDataSourceConfig(@Valid DataSourceConfigCreateReqVO createReqVO);


    /**
     * 更新数据源配置
     *
     * @param updateReqVO 更新信息
     */
    void updateDataSourceConfig(@Valid DataSourceConfigUpdateReqVO updateReqVO);


    /**
     * 删除数据源配置
     *
     * @param id 编号
     */
    void deleteDataSourceConfig(Long id);

    /**
     * 获得数据源配置
     *
     * @param id 编号
     * @return 数据源配置
     */
    DataSourceConfigDO getDataSourceConfig(Long id);
}
