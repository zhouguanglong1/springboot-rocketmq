package com.zhougl.springboot.rocketmq.consumer;

import com.zhougl.springboot.rocketmq.consumer.processor.MessageProcessor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 消息监听器
 *
 * @author zhougl
 * @since 2019/8/9 15:44
 */
@Configuration
public class ConsumeMessageListener implements MessageListenerConcurrently {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeMessageListener.class);

    @Autowired
    private MessageProcessorFactory factory;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {

        if (CollectionUtils.isEmpty(list)) {
            LOGGER.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = list.get(0);
        String s = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("接受到的消息为：{}",s);
        MessageProcessor processor = factory.getProcessor(messageExt.getTopic());
        if(Objects.isNull(processor)){
            LOGGER.info("找不到对应的主题");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return processor.consumeMessage(messageExt);
    }
}
