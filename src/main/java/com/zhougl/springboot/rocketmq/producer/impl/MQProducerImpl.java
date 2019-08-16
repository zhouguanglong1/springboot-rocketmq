package com.zhougl.springboot.rocketmq.producer.impl;

import com.zhougl.springboot.rocketmq.producer.MQProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhougl
 * @since 2019/8/16 11:23
 */
public class MQProducerImpl implements MQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQProducerImpl.class);

    @Autowired
    private DefaultMQProducer mqProducer;

    @Override
    public SendResult sendMsg(String topic, String body) {
        LOGGER.info("事务消息开始发送");
        Message message = new Message("transaction_topic", "*", body.getBytes());
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("事务消息发送失败",e);
        }
        LOGGER.info("事务消息发送成功，消息={}",message);
        return sendResult;
    }
}
