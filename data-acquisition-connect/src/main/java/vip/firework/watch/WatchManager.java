package vip.firework.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 链接监控
 *  避免对主线程产生影响，开启新的线程去处理监控状态
 * @author yongjieshi1
 * @date 2019/3/27 1:42 PM
 */
public class WatchManager{
    private static Logger logger = LoggerFactory.getLogger(WatchManager.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static Wactch wactch=null;
    /**
     * 存储当前链接状态
     */
    private static  Map<String,WatchEntity> cacheWatch = new ConcurrentHashMap<>();

    /**
     * 获取动态代理的监控对象
     * @return
     */
    public static Wactch proxyWatch(){
        if(wactch==null){
            synchronized (WatchManager.class){
                if(wactch==null){
                    WatchImpl watchImpl = new WatchImpl();
                    WatchInvocationHandler invocationHandler = new WatchInvocationHandler(watchImpl,executorService);
                    wactch = (Wactch)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                            watchImpl.getClass().getInterfaces(),invocationHandler);
                }
            }
        }
        return wactch;
    }

}
