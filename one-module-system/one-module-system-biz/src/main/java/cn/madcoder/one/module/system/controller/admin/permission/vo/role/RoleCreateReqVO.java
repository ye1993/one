package cn.madcoder.one.module.system.controller.admin.permission.vo.role;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 角色创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleCreateReqVO extends RoleBaseVO {

}
