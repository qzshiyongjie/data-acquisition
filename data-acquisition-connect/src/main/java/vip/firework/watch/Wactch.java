package vip.firework.watch;

import java.util.Map;

/**
 * 监控
 *
 * @author yongjieshi1
 * @date 2019/3/28 10:43 AM
 */
public interface Wactch {
    /**
     * 客户端链接正常
     * @param address
     */
    void acitive( String address);

    /**
     * 客户端链接不活跃
     * @param address
     */
    void inAcitive(String address);
    /**
     * 客户端链接异常
     * @param address
     */
    void catchExpetion(String address);

    /**
     * 读数据
     *  用于统计当前读了多少数据
     * @param address
     */
    void read(String address);

    /**
     * 打印监控详情
     * @return
     */
    Map<String,WatchEntity> printWatch();
}
