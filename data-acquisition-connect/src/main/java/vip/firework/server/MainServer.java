package vip.firework.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.firework.handler.ClientWatchHandler;
import vip.firework.handler.ServiceHandler;

/**
 * 服务启动
 * @author yongjieshi1
 * @date 2019/3/27 9:42 AM
 */
@Component("mainServer")
public class MainServer extends AbstracrServer {

    @Autowired
    ClientWatchHandler clientWatchHandler;

    @Autowired
    ServiceHandler serviceHandler;

    @Override
    void createPipeline(SocketChannel ch) {
        final ByteBuf startBuf = ch.alloc().buffer(1);
        //开始字节
        startBuf.writeByte(0x26);
        //结束字节1
        final ByteBuf endBuf1 = ch.alloc().buffer(1);
        endBuf1.writeByte(0x23);
        //结束字节2
        final ByteBuf endBuf2 = ch.alloc().buffer(1);
        endBuf2.writeByte(0x40);

        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2000, false, startBuf, endBuf1, endBuf2));
        //链接监控
        ch.pipeline().addLast(clientWatchHandler);
        //业务处理
        ch.pipeline().addLast(serviceHandler);
    }
}
