package cn.madcoder.one.framework.common.validation;


import cn.madcoder.one.framework.common.core.IntArrayValuable;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
//用来表示注解作用范围，超过这个作用范围，编译的时候就会报错
@Retention(RetentionPolicy.RUNTIME)
//注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Documented
//被 @Documented 修饰的注解将包含在 JavaDoc 中 。此注解会被javadoc工具提取成文档。
@Constraint(
        validatedBy = InEnumValidator.class
)
public @interface InEnum {

    /**
     * @return 实现 EnumValuable 接口的
     */
    Class<? extends IntArrayValuable> value();

    String message() default "必须在指定范围 {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
