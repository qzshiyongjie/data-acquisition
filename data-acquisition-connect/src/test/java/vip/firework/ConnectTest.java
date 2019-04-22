package vip.firework;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import vip.firework.listener.ConnectListener;
import vip.firework.listener.impl.DefaultConnectListener;
import vip.firework.server.AbstracrServer;
import vip.firework.server.MainServer;
import vip.firework.watch.WatchManager;

/**
 * 测试
 * @author yongjieshi1
 * @date 2019/3/26 6:22 PM
 */
public class ConnectTest {

    @Test
    public void testConnect() {
        ConnectListener listener = new DefaultConnectListener();
        AbstracrServer mainServer = new MainServer();
        mainServer.start(listener, 9000);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                WatchManager.proxyWatch().printWatch();
            }
        }, 0, 30, TimeUnit.SECONDS);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
