package cn.madcoder.one.module.system.convert.permission;


import cn.madcoder.one.module.system.controller.admin.permission.vo.menu.MenuCreateReqVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.menu.MenuRespVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.menu.MenuSimpleRespVO;
import cn.madcoder.one.module.system.controller.admin.permission.vo.menu.MenuUpdateReqVO;
import cn.madcoder.one.module.system.dal.dataobject.permission.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    MenuRespVO convert(MenuDO bean);


    MenuDO convert(MenuCreateReqVO bean);

    MenuDO convert(MenuUpdateReqVO bean);

    List<MenuRespVO> convertList(List<MenuDO> bean);


    List<MenuSimpleRespVO> convertList02(List<MenuDO> bean);


}
