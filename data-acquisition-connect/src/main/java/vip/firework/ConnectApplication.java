package vip.firework;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import vip.firework.listener.ConnectListener;
import vip.firework.server.AbstracrServer;
import vip.firework.watch.WatchManager;

@SpringBootApplication
public class ConnectApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ConnectApplication.class, args);
        ConnectListener listener = (ConnectListener) context.getBean("defaultConnectListener");
        AbstracrServer mainServer = (AbstracrServer) context.getBean("mainServer");
        mainServer.start(listener, 9000);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                WatchManager.proxyWatch().printWatch();
            }
        }, 0, 30, TimeUnit.SECONDS);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
