package com.zhougl.springboot.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author zhougl
 * @since 2019/8/16 11:26
 */
public interface MQTransactionProducer {
    SendResult sendMsg(String topic, String body);
}
