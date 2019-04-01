package vip.firework;

import org.junit.Test;
import vip.firework.factory.TransformFactory;
import vip.firework.routerbean.BloodPressure;

/**
 * 转化工具测试类
 *
 * @author yongjieshi1
 * @date 2019/3/25 4:11 PM
 */
public class TransformTest {
    @Test
    public void testTransfrom() {
        byte[] source = {0x02, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x34, 0x30, 0x30, 0x38,
                0x30, 0x30, 0x36, 0x30, 0x32, 0x30, 0x31, 0x36, 0x30, 0x36, 0x30, 0x31, 0x31, 0x30, 0x30, 0x32};
        BloodPressure bloodPressure = TransformFactory.getTransBean(source);
        System.out.println(bloodPressure);
    }

    @Test
    public void testSubString() {
        String str = "12331123449";
        System.out.println(str.substring(1, str.length() - 1));
    }
}
