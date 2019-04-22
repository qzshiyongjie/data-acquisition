package vip.firework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * nio测试
 * @author yongjieshi1
 * @date 2019/4/12 5:33 PM
 */
public class NioClientTest {

    static final AtomicInteger num = new AtomicInteger(0);

    @Test
    public void test_NioClientTest() {
        try {
            NioClientTest test = new NioClientTest();
            test.work(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final byte[] source = {0x26, 0x02, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x34, 0x30, 0x30,
            0x38, 0x30, 0x30, 0x36, 0x30, 0x32, 0x30, 0x31, 0x36, 0x30, 0x36, 0x30, 0x31, 0x31, 0x30, 0x30, 0x32, 0x40};

    SocketChannel sc = null;

    Selector selector = null;

    // 发送接收缓冲区
    ByteBuffer send = ByteBuffer.wrap(source);

    boolean writeflag = true;

    ByteBuffer receive = ByteBuffer.allocate("ACK".getBytes().length);

    public final void work(int port) throws IOException {

        try {
            sc = SocketChannel.open();
            selector = selector.open();
            // 注册为非阻塞通道
            sc.configureBlocking(false);

            sc.connect(new InetSocketAddress("localhost", port));

            sc.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set<SelectionKey> selectionKeys = null;
        while (true) {
            // 选择
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                // 必须由程序员手动操作
                it.remove();

                sc = (SocketChannel) key.channel();

                if (key.isConnectable()) {
                    if (sc.isConnectionPending()) {
                        hendlerConneted();
                    }

                }
                else if (key.isReadable()) {
                    heandlerRead();
                }
                else if (key.isWritable()) {
                    handlerWrite();
                }

            }// end while

        }// end while(true)

    }// end work()

    private void handlerWrite() {
        if (writeflag) {
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    receive.flip();
                    try {
                        send.flip();
                        NioClientTest.num.incrementAndGet();
                        sc.write(send);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, 1, 10, TimeUnit.MILLISECONDS);
            writeflag = false;
            return;
        }

    }

    private void heandlerRead() {
        try {
            receive.clear();
            sc.read(receive);
            if (receive.hasArray()) {
                String rsult = new String(receive.array());
                System.out.println("====>" + Thread.currentThread().getName() + rsult + NioClientTest.num.intValue());
            }
            receive.clear();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void hendlerConneted() throws IOException {
        // 结束连接，以完成整个连接过程
        sc.finishConnect();
        System.out.println("connect completely");

        try {
            sc.write(send);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
