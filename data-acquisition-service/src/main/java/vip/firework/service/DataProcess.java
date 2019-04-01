package vip.firework.service;

import vip.firework.routerbean.BaseBean;

/**
 * 数据处理
 *
 * @author yongjieshi1
 * @date 2019/3/29 2:05 PM
 */
public interface DataProcess<T extends BaseBean> {
    /**
     * 数据业务处理
     * @param str
     * @return
     */
    String process(String str);
}
