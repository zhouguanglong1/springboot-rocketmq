package com.zhougl.springboot.rocketmq.consumer;

import com.zhougl.springboot.rocketmq.consumer.processor.MessageProcessor;
import com.zhougl.springboot.rocketmq.consumer.processor.SaleOrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhougl
 * @since 2019/8/9 17:00
 */
@Configuration
public class MessageProcessorFactory {

    @Autowired
    private SaleOrderProcessor saleOrderProcessor;

    MessageProcessor getProcessor(String topic) {
        switch (topic) {
            case "default_topic":
                return saleOrderProcessor;
            default:
                return null;
        }
    }
}
