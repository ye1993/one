package cn.madcoder.one.framework.common.util.string;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StrUtils {



    public static String maxLength(CharSequence str, int maxLength) {
        return StrUtil.maxLength(str, maxLength - 3); // -3 的原因，是该方法会补充 ... 恰好
    }

    /**
     * 给定字符串是否以任何一个字符串开始
     * 给定字符串和数组为空都返回 false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @since
     */
    public static boolean startWithAny(String str, Collection<String> prefixes) {
        if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (StrUtil.startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将包含数字的字符串按照指定的分隔符进行分割，并转换为 Long 类型的列表。
     *
     * @param value     包含数字的字符串。
     * @param separator 分隔符。
     * @return Long 类型的列表。
     */
    public static List<Long> splitToLong(String value, CharSequence separator) {
        // 使用 StrUtil.splitToLong 方法将字符串分割为 long 数组

        long[] longs = StrUtil.splitToLong(value, separator);
        // 将 long 数组转换为 Long 类型的列表

        return Arrays.stream(longs).boxed().collect(Collectors.toList());
    }

    public static List<Integer> splitToInteger(String value, CharSequence separator) {
        int[] integers = StrUtil.splitToInt(value, separator);
        return Arrays.stream(integers).boxed().collect(Collectors.toList());
    }
}
