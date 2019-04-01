package vip.firework.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import vip.firework.process.MessageProcess;
import vip.firework.watch.WatchManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务处理
 *
 * @author yongjieshi1
 * @date 2019/3/26 2:17 PM
 */
@Component("serviceHandler")
@Scope("prototype")
@ChannelHandler.Sharable
public class ServiceHandler extends ChannelHandlerAdapter {
    @Autowired
    private MessageProcess messageProcess;
    private static Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    private Map<String, Object> session = new HashMap<String, Object>();


    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg)  throws Exception{
        ByteBuf in = (ByteBuf) msg;
        String value = "";
        try {
            //获取字节数组
            byte[] byteValue = new byte[in.readableBytes()];
            in.readBytes(byteValue);
            //数据为空时过滤数据
            if (byteValue.length <= 1) {
                return;
            }
            //业务处理，一般要求实时返回
            String result = messageProcess.process(byteValue, session);
            final ByteBuf buf = ctx.alloc().buffer(4);
            buf.writeBytes(result.getBytes());
            ctx.writeAndFlush(buf);
            String address = ctx.channel().remoteAddress().toString();
            WatchManager.proxyWatch().read(address);
        } catch (Exception e) {
            logger.error("ServiceHandler channelRead error {}", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
