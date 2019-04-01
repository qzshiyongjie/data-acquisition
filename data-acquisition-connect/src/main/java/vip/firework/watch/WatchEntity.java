package vip.firework.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.firework.handler.ClientWatchHandler;
import vip.firework.util.DateUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控详情
 *  监控客户端当前状态
 *      活跃，不活跃
 * @author yongjieshi1
 * @date 2019/3/27 1:44 PM
 */
public class WatchEntity {
    private static Logger logger = LoggerFactory.getLogger(WatchEntity.class);
    private String address;
    private ConnetLog currentLog;
    private AtomicInteger readCount = new AtomicInteger();
    private Deque<ConnetLog> connectLogs = new LinkedBlockingDeque<>();

    public ConnetLog getCurrentLog() {
        return currentLog;
    }


    public void addLog(String address,ConnectStatus connectStatus, Date currentTime){
        if(connectLogs.size()>=10){
            connectLogs.pollLast();
            //TODO 持久化
        }
        this.currentLog = new ConnetLog(connectStatus,DateUtil.getDateTimeFormat(new Date()),address);
        this.address=address;
        //非活跃状态复制读次数
        if(connectStatus.equals(ConnectStatus.Active)){
            this.readCount = new AtomicInteger();
        }else {
            this.currentLog.setReadCount(readCount.get());
        }
        connectLogs.addFirst(this.currentLog );

    }
    public int getReadCount(){
        return readCount.get();
    }
    public void incrementRead(){
        readCount.incrementAndGet();
    }
    public  Collection<ConnetLog> printLog(){
        Collection<ConnetLog> printLogs = Collections.synchronizedCollection(connectLogs);
        logger.info(" printLog start address {} connectLogs size {}==> ", address,connectLogs.size());
        for(ConnetLog connetLog:printLogs){
            logger.info("==>{}",connetLog);
        }
        logger.info(" printLog off address {} ==>", address);
        return printLogs;
    }
}
