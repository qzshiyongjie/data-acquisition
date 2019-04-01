package vip.firework.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vip.firework.listener.ConnectListener;

import java.util.concurrent.CountDownLatch;
@Component("defaultConnectListener")
public class DefaultConnectListener implements ConnectListener {
    private static Logger logger = LoggerFactory.getLogger(DefaultConnectListener.class);
    private CONNECT_STATUS connectStatus;

    enum CONNECT_STATUS {
        SUCCESS(1, "success"),
        FAIL(2, "fail"),
        EXCEPTION(3, "exception");

        CONNECT_STATUS(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        int status;
        String msg;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "CONNECT_STATUS{" +
                    "status=" + status +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void success() {
        connectStatus = CONNECT_STATUS.SUCCESS;
        latch.countDown();

    }

    @Override
    public void fail() {
        connectStatus = CONNECT_STATUS.FAIL;
        latch.countDown();

    }

    @Override
    public void exception(Exception e) {
        connectStatus = CONNECT_STATUS.EXCEPTION;
        latch.countDown();
    }


    public String connectStatus() {
        try {
            latch.await();
            logger.info("connect status is {}", connectStatus);
            return connectStatus.getMsg();
        } catch (InterruptedException e) {
            logger.error("get connectStatus error", e);
        }
        return null;
    }

}
