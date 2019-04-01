# 用来做什么
用于与tcp的客户端进行链接，采集tcp客户端提供的数据
# 工程介绍
工程分为四个部分
* data-acquisition-api 提供给公共的接口
* data-acquisition-core 将客户端提交的16进制数据转为javaBean的核心实现，以及将解析的javabean放到消息队列
* data-acquisition-connect 用户客户端链接并且收集数据
* data-acquisition-service 这部分用于接受消息队列的javaBean并且对javaBean做业务处理，比如持久化等
# 框架使用
SpringBoot
netty
rabbitmq
# 实现方案
## 协议定义
用户客户端与服务端的协议。
```
开始码（两位） 指令码（两位） 机器编号 （九位）高压（三位）低压（三位）心率（三位）上传时间（十二位）
10进制 &123456789140080060201606011002@
& （开始码）（指令码16进制02，字符无法打印）123456789（机器编号）140（高压） 080（低压） 060（心率） 201606011002 （上传时间） @（结束码）
16进制 26 02 31 32 33 34 35 36 37 38 39 31 34 30 30 38 30 30 36 30 32 30 31 36 30 36 30 31 31 30 30 32 40
```
起始字节，结束自己字节，项目案列中的起始字节为0x26,结束字节为0x40
第二个字节为路由字节，比如0x02
自定义实现字节 第三位到倒数第二位
## netty解包，链接
```
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
```
根据协议定义的起始字节，结束字节进行解包操作
## 链接监控
用于每个tcp状态的监控，比如是否活跃，在链接过程中读取多少次，参考Wactch中的定义
详情参考日志
```
address /10.10.56.93 ,status ConnetLog{status=Active, time='2019-04-01 13:50:57', address='/10.10.56.93:52453', readCount=0} readCount 11
printLog start address /10.10.56.93:52453 connectLogs size 3==>
 ==>ConnetLog{status=Active, time='2019-04-01 13:50:57', address='/10.10.56.93:52453', readCount=0}
 ==>ConnetLog{status=Inactive, time='2019-04-01 13:50:55', address='/10.10.56.93:52436', readCount=0}
 ==>ConnetLog{status=Active, time='2019-04-01 13:49:53', address='/10.10.56.93:52436', readCount=0}
printLog off address /10.10.56.93:52453 ==>
```
## 数据解析
用于将字符串转化为特定的javaBean
* @interface Router
用于定于 处理类，路由标志，路由的开始字节，结束字节
* @interface RouterSplite
字符串分割 用于定义字符的开始位置，结束位置
```
@Router(processClass = BeanProcessNameConstant.BEAN_NAME_BLOODPROCESS, routeKey = {0x02}, start = 0, end = 1)
public class BloodPressure extends BaseBean {

    /**
     * 机器代码
     */

    private String machineCode;

    /**
     * 高压
     */
    private String up;

    /**
     * 低压
     */
    private String down;

    /**
     * 上传时间
     */
    private String uptime;

    private String heartRate;

    public String getMachineCode() {
        return machineCode;
    }

    @RouterSplite(start = 0, end = 9)
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getUp() {
        return up;
    }

    @RouterSplite(start = 9, end = 12)
    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    @RouterSplite(start = 12, end = 15)
    public void setDown(String down) {
        this.down = down;
    }

    public String getHeartRate() {
        return heartRate;
    }

    @RouterSplite(start = 15, end = 18)
    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getUptime() {
        return uptime;
    }

    @RouterSplite(start = 18, end = 30)
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    @Override
    public String toString() {
        return "BloodPressure{" + "machineCode='" + machineCode + '\'' + ", up='" + up + '\'' + ", down='" + down + '\''
                + ", uptime='" + uptime + '\'' + ", heartRate='" + heartRate + '\'' + '}';
    }
}
```
## 解析的消息发送到消息队列
* 消息队列配置
参考 application.properties 中的实现
```
spring.application.name=spirng-boot-rabbitmq-sender
spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```
* 从消息队列中获取数据
参考 data-acquisition-service 中 BloodPressureService的实现
```
@Component
@RabbitListener(queues = BeanProcessNameConstant.BEAN_NAME_BLOODPROCESS)
public class BloodPressureService implements DataProcess<BloodPressure> {

    private static Logger logger = LoggerFactory.getLogger(BloodPressureService.class);

    @Override
    @RabbitHandler
    public String process(String str) {
        BloodPressure bean = JSON.parseObject(str, BloodPressure.class);
        logger.info("BloodPressureService process recive data {}", bean);
        return null;
    }
}
```

# 如何使用？
* 1 定义协议
* 2 定义解析类 继承自BaseBean 参考BloodPressure（必须定义在vip.firework.routerbean这个包底下）
* 3 定义业务处理类 实现DataProcess接口，参考BloodPressureService
# 启动顺序
* 1 ConnectApplication 启动
* 2 ServiceApplication 启动
* 3 data-acquisition-connect 如果需要测试，启动ClientTest
# 后续内容
* 监控可视化
* 高并发性能测试



