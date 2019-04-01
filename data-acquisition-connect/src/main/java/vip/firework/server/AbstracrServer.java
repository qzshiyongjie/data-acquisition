package vip.firework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.firework.handler.ServiceHandler;
import vip.firework.listener.ConnectListener;

import java.util.concurrent.Executors;

public abstract   class AbstracrServer implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(AbstracrServer.class);
    private EventLoopGroup bossgroup = new NioEventLoopGroup();
    private EventLoopGroup workergroup = new NioEventLoopGroup();
    private ConnectListener listener;
    private Integer port;
    private boolean isStart = false;

    public boolean isStart() {
        return isStart;
    }

    public void start(ConnectListener listener,Integer port){
        this.listener=listener;
        this.port=port;
        Executors.newSingleThreadExecutor().execute(this);
    }
    @Override
    public void run() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossgroup, workergroup).channel(NioServerSocketChannel.class).
                    option(ChannelOption.SO_BACKLOG, 100).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    createPipeline(ch);
                }
            });
            ChannelFuture f = bootstrap.bind(port).sync();//绑定端口，同步等待成功
            logger.info("netty sever start port : {}", port);
            if (f.isSuccess()) {
                isStart = true;
                logger.info("netty sever connect success");
                listener.success();//成功时事件处理
            } else if (f.isCancelled()) {
                logger.info("netty sever connect fail");
                listener.fail();//失败事件处理
            }
            f.channel().closeFuture().sync();//等待服务端监听端口关闭
        } catch (Exception e) {
            logger.error("netty sever connect error", e);
            listener.exception(e);//异常事件处理
        } finally {
            stop();
        }
    }

    public synchronized boolean stop() {
        if (isStart) {
            Future bossFuture = bossgroup.shutdownGracefully();
            Future workerFuture = workergroup.shutdownGracefully();
            logger.info("begin stopping netty server");
            try {
                //等待获取结果
                bossFuture.get();
                workerFuture.get();
                isStart = false;
                logger.info("netty server stop success");
                return bossgroup.isShutdown() && workergroup.isShutdown();
            } catch (Exception e) {
                logger.error("netty server stop error {}", e);
                return false;
            }
        } else {
            return false;
        }
    }

    abstract void createPipeline(SocketChannel socketChannel);
}
