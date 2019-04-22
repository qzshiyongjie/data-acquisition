package vip.firework;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    final byte[] req = {0x26, 0x02, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x34, 0x30, 0x30, 0x38,
            0x30, 0x30, 0x36, 0x30, 0x32, 0x30, 0x31, 0x36, 0x30, 0x36, 0x30, 0x31, 0x31, 0x30, 0x30, 0x32, 0x40};

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught {} ,error message {}", ctx, cause);
//        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        logger.info("channel active");
        ctx.executor().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                logger.info("start send data");
                ByteBuf firestMessage = Unpooled.buffer(req.length);
                firestMessage.writeBytes(req);
                ctx.writeAndFlush(firestMessage);
            }
        }, 0, 5, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[ buf.readableBytes() ];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        logger.info("channelRead message {}", body);
    }
}
