package vip.firework.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.firework.handler.ClientWatchHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 动态代理类
 * 避免对主业务流程产生影响，开启新的线程处理
 * @author yongjieshi1
 * @date 2019/3/28 10:56 AM
 */
public class WatchInvocationHandler implements InvocationHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientWatchHandler.class);

    private Wactch wactch;
    private ExecutorService executorService;

    public WatchInvocationHandler(Wactch wactch, ExecutorService executorService) {
        this.wactch = wactch;
        this.executorService = executorService;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final  Object[] args) throws Throwable {
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object result;
                logger.info("==>Watch methon {} start",method.getName());
                result =  method.invoke(wactch,args);
                logger.info("<==Watch methon {} end",method.getName());
                return result;
            }
        };
        Future<Object> future = executorService.submit(callable);
        return future.get();
    }

}
