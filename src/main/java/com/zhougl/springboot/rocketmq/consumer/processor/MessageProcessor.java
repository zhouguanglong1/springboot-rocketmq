package com.zhougl.springboot.rocketmq.consumer.processor;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author zhougl
 * @since 2019/8/9 16:54
 */
public interface MessageProcessor {

    /**
     * 消费消息
     *
     * @param messageExt
     * @return
     */
    ConsumeConcurrentlyStatus consumeMessage(MessageExt messageExt);
}
