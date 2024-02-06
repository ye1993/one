package cn.madcoder.one.module.system.convert.ip;


import cn.madcoder.one.framework.ip.core.Area;
import cn.madcoder.one.framework.ip.core.enums.AreaTypeEnum;
import cn.madcoder.one.module.system.controller.admin.ip.vo.AreaNodeRespVO;
import cn.madcoder.one.module.system.controller.admin.ip.vo.AreaNodeSimpleRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public interface AreaConvert {

    AreaConvert INSTANCE = Mappers.getMapper(AreaConvert.class);

    List<AreaNodeRespVO> convertList(List<Area> list);

    List<AreaNodeSimpleRespVO> convertList2(List<Area> list);

    @Mapping(source = "type", target = "leaf")
    AreaNodeSimpleRespVO convert(Area area);

    default Boolean convertAreaType(Integer type) {
        return Objects.equals(AreaTypeEnum.DISTRICT.getType(), type);
    }


}
