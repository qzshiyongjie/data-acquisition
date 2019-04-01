package vip.firework.routerbean;

import vip.firework.annotation.Router;
import vip.firework.annotation.RouterSplite;
import vip.firework.constant.BeanProcessNameConstant;

/**
 * 血压数据
 * @author yongjieshi1
 * @date 2019/3/25 10:47 AM
 */
@Router(processClass = BeanProcessNameConstant.BEAN_NAME_BLOODPROCESS, routeKey = {0x02}, start = 0, end = 1)
public class BloodPressure extends BaseBean {

    /**
     * 机器代码
     */

    private String machineCode;

    /**
     * 高压
     */
    private String up;

    /**
     * 低压
     */
    private String down;

    /**
     * 上传时间
     */
    private String uptime;

    private String heartRate;

    public String getMachineCode() {
        return machineCode;
    }

    @RouterSplite(start = 0, end = 9)
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getUp() {
        return up;
    }

    @RouterSplite(start = 9, end = 12)
    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    @RouterSplite(start = 12, end = 15)
    public void setDown(String down) {
        this.down = down;
    }

    public String getHeartRate() {
        return heartRate;
    }

    @RouterSplite(start = 15, end = 18)
    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getUptime() {
        return uptime;
    }

    @RouterSplite(start = 18, end = 30)
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    @Override
    public String toString() {
        return "BloodPressure{" + "machineCode='" + machineCode + '\'' + ", up='" + up + '\'' + ", down='" + down + '\''
                + ", uptime='" + uptime + '\'' + ", heartRate='" + heartRate + '\'' + '}';
    }
}
