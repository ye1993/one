package cn.madcoder.one.framework.common.util.collection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import static cn.madcoder.one.framework.common.util.collection.CollectionUtils.convertList;


/**
 * Array 工具类
 *
 * @author
 */
public class ArrayUtils {

    /**
     * 将 object 和 newElements 合并成一个数组
     *
     * @param object 对象
     * @param newElements 数组
     * @param <T> 泛型
     * @return 结果数组
     */
    @SafeVarargs
    public static <T> Consumer<T>[] append(Consumer<T> object, Consumer<T>... newElements) {
        if (object == null) {
            return newElements;
        }
        Consumer<T>[] result = ArrayUtil.newArray(Consumer.class, 1 + newElements.length);
        result[0] = object;
        System.arraycopy(newElements, 0, result, 1, newElements.length);
        return result;
    }

    //将一个类型的集合转换为另一类型的集合的通用方法。
    public static <T, V> V[] toArray(Collection<T> from, Function<T, V> mapper) {
        return toArray(convertList(from, mapper));
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> from) {
        if (CollectionUtil.isEmpty(from)) {
            return (T[]) (new Object[0]);
        }
        //from.iterator(): 获取集合 from 的迭代器。
        //(Class<T>): 这是一个类型转换。它试图将 IterUtil.getElementType(...) 的结果强制转换为 Class<T> 类型。这个转换假定元素类型是 T 类型的。
        //这个表达式实际上是在尝试获取集合 from 中元素的类。如果 from 是一个特定类型的集合，这个表达式将尝试将推断的元素类型强制转换为该特定类型。
        return ArrayUtil.toArray(from, (Class<T>) IterUtil.getElementType(from.iterator()));
    }

    public static <T> T get(T[] array, int index) {
        if (null == array || index >= array.length) {
            return null;
        }
        return array[index];
    }

}
