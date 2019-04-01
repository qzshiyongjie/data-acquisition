package vip.firework.routerbean;

import java.io.Serializable;

/**
 * BaseBean
 *
 * @author yongjieshi1
 * @date 2019/3/25 10:25 AM
 */
public class BaseBean implements Serializable {
    /**
     * 基础数据-16进制
     */
    private byte[] baseData;
    /**
     * 最终处理类
     */
    private String processClass;

    public byte[] getBaseData() {
        return baseData;
    }

    public void setBaseData(byte[] baseData) {
        this.baseData = baseData;
    }

    public String getProcessClass() {
        return processClass;
    }

    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }
}
