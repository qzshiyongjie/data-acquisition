package vip.firework.process;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.firework.constants.RouterConstans;
import vip.firework.factory.TransformFactory;
import vip.firework.routerbean.BaseBean;
import vip.firework.util.HexUtil;

import java.util.Map;

/**
 * 默认业务解析处理
 * 将收集到的数据发送给service层
 * @author yongjieshi1
 * @date 2019/3/26 2:54 PM
 */
@Component("defaultMessageProcess")
public class DefaultMessageProcess implements MessageProcess {
    private static Logger logger = LoggerFactory.getLogger(DefaultMessageProcess.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String process(byte[] source, Map<String, Object> session) {
        String result = RouterConstans.COMMOND_BOX_OK;
        try {
            //获取消息
            BaseBean baseBean = TransformFactory.getTransBean(source);
            logger.error("DefaultMessageProcess process baseBean {} ",baseBean);
            //根据要求是否要添加到消息队列
            String json = JSON.toJSONString(baseBean);
            rabbitTemplate.convertAndSend(baseBean.getProcessClass(),json);
            //消息序列化方式
        } catch (Exception e) {
            String data = HexUtil.toStringHex(HexUtil.bytesToHexString(source));
            logger.error("DefaultMessageProcess processerror,data {} ,error {} ",
                    data, e);
        }
        return result;
    }
}
