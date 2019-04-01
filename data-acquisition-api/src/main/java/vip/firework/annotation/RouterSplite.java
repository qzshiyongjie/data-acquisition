package vip.firework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由分割
 *
 * @author yongjieshi1
 * @date 2019/3/25 10:17 AM
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterSplite {
    /**
     * 字符开始截取位置
     *
     * @return
     */
    int start();

    /**
     * 字符结束位置 小于0时从后往前截取
     *
     * @return
     */
    int end();
}
