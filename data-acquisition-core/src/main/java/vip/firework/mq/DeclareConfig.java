package vip.firework.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vip.firework.factory.TransformFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 声明配置
 *
 * @author yongjieshi1
 * @date 2019/3/28 3:56 PM
 */
@Component
public class DeclareConfig {
    private final String ExchageName = "firework.direct.exchange";
    /**
     * 声明direct类型的Exchange
     * @return
     */
    @Bean
    public Exchange directExchange(){
        return new DirectExchange(ExchageName,true,false);
    }
    @Bean
    public List<Queue> queues(){
        List<Queue> queueList = new ArrayList<>();
        List<String> queueNames = TransformFactory.getQueueName();
        for(String queueName:queueNames){
            queueList.add(new Queue(queueName,true));
        }
        return queueList;
    }
    @Bean
    public List<Binding> bindings(){
        List<Binding> bindingList = new ArrayList<>();
        List<String> queueNames = TransformFactory.getQueueName();
        for(String queueName:queueNames){
            bindingList.add(BindingBuilder.bind(new Queue(queueName,true)).
                    to(new DirectExchange(ExchageName,true,false)).with(queueName));
        }
        return bindingList;
    }

}
