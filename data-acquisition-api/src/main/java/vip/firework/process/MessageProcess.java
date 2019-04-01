package vip.firework.process;

import java.util.Map;

/**
 * 消息处理
 *
 * @author yongjieshi1
 * @date 2019/3/26 2:27 PM
 */
public interface MessageProcess {
    /**
     * 消息处理
     *
     * @param source
     * @param session 用于同一个链接中存储数据
     * @return
     */
    String process(byte[] source, Map<String, Object> session);
}
