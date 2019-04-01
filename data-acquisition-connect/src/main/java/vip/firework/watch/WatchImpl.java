package vip.firework.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控实现类
 *
 * @author yongjieshi1
 * @date 2019/3/28 10:51 AM
 */
public class WatchImpl implements Wactch {
    private static Logger logger = LoggerFactory.getLogger(WatchImpl.class);
    /**
     * 存储当前链接状态
     */
    private static Map<String,WatchEntity> cacheWatch = new ConcurrentHashMap<>();
    /**
     * 客户端链接正常
     * @param address
     */
    @Override
    public void  acitive(final String address){
        logger.info("WatchManager acitive address {}",address);
        String ip = address.split(":")[0];
        WatchEntity entity = getWatchEntity(ip);
        entity.addLog(address,ConnectStatus.Active,new Date());
        cacheWatch.put(ip,entity);
    }

    /**
     * 客户端链接不活跃
     * @param address
     */
    @Override
    public void  inAcitive(String address){
        logger.info("WatchManager inAcitive address {}",address);
        String ip = address.split(":")[0];
        WatchEntity entity = getWatchEntity(ip);
        entity.addLog(address,ConnectStatus.Inactive,new Date());
        cacheWatch.put(ip,entity);
    }

    /**
     * 客户端链接异常
     * @param address
     */
    @Override
    public void catchExpetion(String address){
        logger.info("WatchManager catchExpetion address {}",address);
        String ip = address.split(":")[0];
        WatchEntity entity = getWatchEntity(ip);
        entity.addLog(address,ConnectStatus.Exception,new Date());
        cacheWatch.put(ip,entity);
    }

    @Override
    public void read(String address){
        String ip = address.split(":")[0];
        WatchEntity entity = getWatchEntity(ip);
        entity.incrementRead();
        cacheWatch.put(ip,entity);
    }

    /**
     * 打印监控
     * @return
     */
    @Override
    public Map<String,WatchEntity> printWatch(){
        Map<String,WatchEntity> result = Collections.synchronizedMap(cacheWatch);
        for(Map.Entry<String,WatchEntity> entry:result.entrySet()){
            WatchEntity entity = entry.getValue();
            logger.info("==>address {} ,status {} readCount {}",entry.getKey(),entity.getCurrentLog(),entity.getReadCount());
            entry.getValue().printLog();
        }
        return result;
    }
    private static WatchEntity getWatchEntity(String address) {
        WatchEntity entity = cacheWatch.get(address);
        if(entity == null){
            synchronized (WatchManager.class){
                if(entity==null){
                    entity = new WatchEntity();
                }
            }
        }
        return entity;
    }

}
