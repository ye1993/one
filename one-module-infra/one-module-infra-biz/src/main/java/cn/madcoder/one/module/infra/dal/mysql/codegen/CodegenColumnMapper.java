package cn.madcoder.one.module.infra.dal.mysql.codegen;


import cn.madcoder.one.framework.mybatis.core.mapper.BaseMapperX;
import cn.madcoder.one.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.madcoder.one.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodegenColumnMapper extends BaseMapperX<CodegenColumnDO> {


    default void deleteListByTableId(Long tableId) {
        delete(new LambdaQueryWrapperX<CodegenColumnDO>()
                .eq(CodegenColumnDO::getTableId, tableId));
    }
}
