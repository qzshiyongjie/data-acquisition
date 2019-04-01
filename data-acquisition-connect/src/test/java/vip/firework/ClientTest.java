package vip.firework;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

/**
 * 链接测试
 * @author yongjieshi1
 * @date 2019/4/1 3:25 PM
 */
public class ClientTest {

    @Test
    public void testSend() {
        try {
            byte[] source = {0x26, 0x02, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x34, 0x30, 0x30,
                    0x38, 0x30, 0x30, 0x36, 0x30, 0x32, 0x30, 0x31, 0x36, 0x30, 0x36, 0x30, 0x31, 0x31, 0x30, 0x30,
                    0x32, 0x40};
            //1.创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("localhost", 9000);
            //2.获取输出流，向服务器端发送信息
            OutputStream os = socket.getOutputStream();//字节输出流
            os.write(source);
            os.flush();
            socket.shutdownOutput();//关闭输出流
            //3.获取输入流，并读取服务器端的响应信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("我是客户端，服务器说：" + info);
            }
            //4.关闭资源
            br.close();
            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
