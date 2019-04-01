package vip.firework.util;

import java.util.Arrays;

/**
 * 字节工具类
 *
 * @author yongjieshi1
 * @date 2019/3/25 11:44 AM
 */
public class ByteUtil {
    public static byte[] getSubByte(byte[] bytes, int start, int end) {
        if (bytes.length < end) {
            throw new RuntimeException("长度超限");
        }
        return Arrays.copyOfRange(bytes, start, end);
    }
}
