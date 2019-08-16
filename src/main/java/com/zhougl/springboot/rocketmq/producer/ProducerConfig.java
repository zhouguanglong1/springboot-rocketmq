package com.zhougl.springboot.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 如果有多个业务场景需要用到事务消息的生产者的话，就需要定义多个生产者分别发送自己的事务消息
 *
 * @author zhougl
 * @since 2019/8/16 11:01
 */
@Configuration
public class ProducerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerConfig.class);

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private TransactionListener transactionListener;

    @Bean
    public DefaultMQProducer mqProducer(){
        DefaultMQProducer mqProducer = new DefaultMQProducer("default_group");
        mqProducer.setNamesrvAddr(namesrvAddr);
        try {
            mqProducer.start();
            LOGGER.info("生产者启动成功,group={},namesrvAddr={}","default_group",namesrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("启动失败",e);
        }
        return mqProducer;
    }

    @Bean
    public TransactionMQProducer transactionMQProducer(){
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("transaction_default_group");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        transactionMQProducer.setExecutorService(executorService);
        transactionMQProducer.setTransactionListener(transactionListener);
        try {
            transactionMQProducer.start();
            LOGGER.info("启动成功");
        } catch (MQClientException e) {
            LOGGER.error("启动失败",e);
        }
        return transactionMQProducer;
    }
}
