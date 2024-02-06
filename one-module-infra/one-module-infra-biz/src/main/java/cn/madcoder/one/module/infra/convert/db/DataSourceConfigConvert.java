package cn.madcoder.one.module.infra.convert.db;

import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigCreateReqVO;
import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigRespVO;
import cn.madcoder.one.module.infra.controller.admin.db.vo.DataSourceConfigUpdateReqVO;
import cn.madcoder.one.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据源配置 Convert
 *
 * @author mad
 */
@Mapper
public interface DataSourceConfigConvert {

    DataSourceConfigConvert INSTANCE = Mappers.getMapper(DataSourceConfigConvert.class);



    List<DataSourceConfigRespVO> convertList(List<DataSourceConfigDO> list);


    DataSourceConfigDO convert(DataSourceConfigCreateReqVO bean);

    DataSourceConfigDO convert(DataSourceConfigUpdateReqVO bean);


    DataSourceConfigRespVO convert(DataSourceConfigDO bean);



}
