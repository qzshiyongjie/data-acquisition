package vip.firework.listener;

public interface ConnectListener {
    void success();//成功

    void fail();//失败

    void exception(Exception e);//出现异常
}
