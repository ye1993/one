package cn.madcoder.one.framework.common.validation;

import cn.madcoder.one.framework.common.core.IntArrayValuable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InEnumValidator implements ConstraintValidator<InEnum, Integer> {

    private List<Integer> values;

    //实现 #isValid(value, context) 方法，实现校验参数值，是否在 values 范围内
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        // 为空时，默认不校验，即认为通过
        if (value == null) {
            return true;
        }
        // 校验通过
        if (values.contains(value)) {
            return true;
        }
        // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
        constraintValidatorContext.disableDefaultConstraintViolation(); // 禁用默认的 message 的值
        constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate()
                .replaceAll("\\{value}", values.toString())).addConstraintViolation(); // 重新添加错误提示语句
        return false;
    }

    //获得 @InEnum 注解的 values() 属性，获得值数组，设置到 values 属性种。
    @Override
    public void initialize(InEnum annotation) {
        IntArrayValuable[] values = annotation.value().getEnumConstants();
        if (values.length == 0) {
            this.values = Collections.emptyList();
        } else {
            this.values = Arrays.stream(values[0].array()).boxed().collect(Collectors.toList());
        }
    }
}
