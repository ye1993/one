package cn.madcoder.one.module.system.enums.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum SexEnum {

    MAN(1),
    FEMALE(2),
    UNKNOWN(3);

    /**
     * 性别
     */
    private final Integer sex;



}
