package vip.firework.service.impl;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vip.firework.constant.BeanProcessNameConstant;
import vip.firework.routerbean.BloodPressure;
import vip.firework.service.DataProcess;

/**
 * 血压业务逻辑处理
 * @author yongjieshi1
 * @date 2019/3/29 2:14 PM
 */
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
