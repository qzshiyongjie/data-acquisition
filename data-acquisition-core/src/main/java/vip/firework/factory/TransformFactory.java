package vip.firework.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.firework.annotation.Router;
import vip.firework.annotation.RouterSplite;
import vip.firework.routerbean.BaseBean;
import vip.firework.util.AnnoManageUtil;
import vip.firework.util.HexUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转化工厂类
 *
 * @author yongjieshi1
 * @date 2019/3/25 11:00 AM
 */
public class TransformFactory {
    private static Logger logger = LoggerFactory.getLogger(TransformFactory.class);

    private static Map<String, Class<?>> cacheRouter = new ConcurrentHashMap<>();

    private static List<String> cacheQueueName;
    /**
     * 路由截取开始位置
     */
    private static Integer start;
    /**
     * 路由截取结束位置
     */
    private static Integer end;

    static {
        initCacheRouter();
    }

    public TransformFactory() {

    }

    private static void initCacheRouter() {
        String packageName = "vip.firework.routerbean";
        Set<Class<?>> classes = AnnoManageUtil.getClassUnderPackage(packageName, Router.class);
        for (Class<?> clazz : classes) {
            Router router = clazz.getAnnotation(Router.class);
            cacheRouter.put(HexUtil.bytesToHexString(router.routeKey()), clazz);
            if (start == null) {
                start = router.start();
                end = router.end();
            }
        }
    }

    /**
     * 获取队列名称，根据Router注解中processClass
     * @return
     */
    public static List<String> getQueueName() {
        if(cacheQueueName == null){
            synchronized (TransformFactory.class){
                if(cacheQueueName == null){
                    cacheQueueName = new ArrayList<>();
                    for (Map.Entry<String, Class<?>> entry : cacheRouter.entrySet()) {
                        Class<?> clazz = entry.getValue();
                        Router router = clazz.getAnnotation(Router.class);
                        cacheQueueName.add(router.processClass());
                    }
                }
            }
        }
        return cacheQueueName;
    }

    /**
     * 获取转化类
     *
     * @param sourcesBytes
     * @param <T>
     */
    public static synchronized <T extends BaseBean> T getTransBean(byte[] sourcesBytes) {
        if (start != null) {
            byte[] key = Arrays.copyOfRange(sourcesBytes, start, end);
            Class<?> clazz = cacheRouter.get(HexUtil.bytesToHexString(key));
            if (clazz != null) {
                T baseBean = null;
                try {
                    byte[] value = Arrays.copyOfRange(sourcesBytes, end, sourcesBytes.length);
                    String result = HexUtil.toStringHex(HexUtil.bytesToHexString(value));
                    baseBean = (T) clazz.newInstance();
                    baseBean.setProcessClass(clazz.getAnnotation(Router.class).processClass());
                    baseBean.setBaseData(value);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        RouterSplite splite = method.getAnnotation(RouterSplite.class);
                        if (splite != null) {
                            int start = splite.start();
                            int end = splite.end();
                            if (end < 0) {
                                end = result.length() + end;
                            }
                            if (start < 0) {
                                start = result.length() + start;
                            }
                            if (start >= end) {
                                throw new RuntimeException("字符开始位置" + start + "大于字符结束位置" + end);
                            }
                            method.invoke(baseBean, result.substring(splite.start(), splite.end()));
                        }
                    }
                } catch (Exception e) {
                    logger.error("TransformFactory getTransBean error {}", e);
                }
                return baseBean;
            }
        } else {
            throw new RuntimeException("未找到对应的解析类");
        }
        return null;
    }
}
