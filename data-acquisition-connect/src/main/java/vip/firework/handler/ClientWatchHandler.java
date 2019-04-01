package vip.firework.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import vip.firework.watch.WatchManager;


/**
 * 客户端监控
 *
 * @author yongjieshi1
 * @date 2019/3/27 10:04 AM
 */
@Component("clientWatchHandler")
@Scope("prototype")
@ChannelHandler.Sharable
public class ClientWatchHandler extends ChannelHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ClientWatchHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String address = ctx.channel().remoteAddress().toString();
        WatchManager.proxyWatch().acitive(address);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String address = ctx.channel().remoteAddress().toString();
        WatchManager.proxyWatch().inAcitive(address);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.info("ServiceHandler exceptionCaught {}", cause);
        String address = ctx.channel().remoteAddress().toString();
        ctx.close();
        ctx.fireExceptionCaught(cause);
        WatchManager.proxyWatch().catchExpetion(address);
    }
}
