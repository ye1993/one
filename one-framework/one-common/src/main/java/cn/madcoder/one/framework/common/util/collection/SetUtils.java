package cn.madcoder.one.framework.common.util.collection;

import cn.hutool.core.collection.CollUtil;

import java.util.Set;

/**
 * Set 工具类
 *
 * @author mad
 */
public class SetUtils {

    //在Java编程语言中， 当一个 可变泛型参数 指向一个 无泛型参数 时，堆污染(Heap Pollution)就有可能发生。
    @SafeVarargs
    public static <T> Set<T> asSet(T... objs) {
        return CollUtil.newHashSet(objs);
    }
}
