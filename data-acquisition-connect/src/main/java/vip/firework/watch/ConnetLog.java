package vip.firework.watch;

/**
 * 链接日志
 * @author yongjieshi1
 * @date 2019/3/27 2:05 PM
 */
public class ConnetLog {
    private ConnectStatus status;
    private String time;
    private String address;
    private Integer readCount=0;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public ConnetLog(ConnectStatus status, String time, String address) {
        this.status = status;
        this.time = time;
        this.address = address;
    }

    public ConnectStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectStatus status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ConnetLog{" +
                "status=" + status +
                ", time='" + time + '\'' +
                ", address='" + address + '\'' +
                ", readCount=" + readCount +
                '}';
    }
}
