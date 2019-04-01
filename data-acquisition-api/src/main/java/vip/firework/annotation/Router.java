package vip.firework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由
 *
 * @author yongjieshi1
 * @date 2019/3/25 10:08 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Router {
    /**
     * 处理类
     *
     * @return
     */
    String processClass();

    /**
     * 路由标志，某一长度的16进制数组
     *
     * @return
     */
    byte[] routeKey();

    /**
     * 路由开始位置 小于0时 变为str.length()+start
     *
     * @return
     */
    int start();

    /**
     * 路由结束标志 小于0时 变为str.length()+end
     *
     * @return
     */
    int end();

}
