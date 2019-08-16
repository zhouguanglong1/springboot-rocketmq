package com.zhougl.springboot.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhougl
 * @since 2019/8/9 15:46
 */
@Configuration
public class RocketMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumer.class);

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private ConsumeMessageListener consumeMessageListener;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("default_group");
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.registerMessageListener(consumeMessageListener);
        try {
            // 开单
            consumer.subscribe("default_topic", "*");
            // 取消开单
            consumer.subscribe("special_topic", "tag1 || tag2");
            consumer.start();
            LOGGER.info("consumer is start !!! groupName:{},namesrvAddr:{}", "default_group", namesrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("consumer is start !!! groupName:{},namesrvAddr:{}", "default_group", namesrvAddr, e);
        }
        return consumer;
    }
}
