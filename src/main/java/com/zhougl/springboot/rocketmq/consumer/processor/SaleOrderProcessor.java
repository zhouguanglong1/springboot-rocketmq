package com.zhougl.springboot.rocketmq.consumer.processor;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 *
 *
 * @author zhougl
 * @since 2019/8/9 16:55
 */
@Configuration
public class SaleOrderProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleOrderProcessor.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(MessageExt messageExt) {
        LOGGER.info("开始消费消息 topic = {}", messageExt.getTopic());
        byte[] body = messageExt.getBody();
        String message = "";
        try {
            message = new String(body, StandardCharsets.UTF_8);
            LOGGER.info("消费消息结果 topic = {}, result = {}", messageExt.getTopic(), true);
        } catch (Exception e) {
            LOGGER.info("消息消费失败， topic = {},message={},重试次数 = {}", messageExt.getTopic(), message, messageExt.getReconsumeTimes());
            LOGGER.error("消息消费失败", e);
            // 如果重试了三次就返回成功
            if (messageExt.getReconsumeTimes() == 3) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public static void main(String[] args) {
        String message = "{\n" +
                "  \"tenantCode\": \"10000\",\n" +
                "  \"baasTenantId\": \"10000\",\n" +
                "  \"buyerDistributorId\": \"30\", \n" +
                "  \"buyerDistributorName\": \"30\", \n" +
                "  \"deliveryAddress\": null, \n" +
                "  \"deliveryConsignee\": null, \n" +
                "  \"deliveryDistrictId\": null, \n" +
                "  \"deliveryMobilePhone\": null, \n" +
                "  \"deliveryType\": 2,\n" +
                "  \"expectedOutboundDate\": \"2019-08-16 00:00:00\",\n" +
                "  \"fromDepotId\": \"20\",\n" +
                "  \"saleOrderId\": \"367997019241234568\", \n" +
                "  \"saleOrderNo\": \"102019081300011124\", \n" +
                "  \"sellerDistributorId\": \"20\", \n" +
                "  \"sellerDistributorName\": \"20\", \n" +
                "  \"toDepotId\": \"30\", \n" +
                "  \"saleOrderItemList\": [\n" +
                "    {\n" +
                "      \"skuId\": \"202\",\n" +
                "      \"skuName\": \"OPPO R15\", \n" +
                "      \"skuPrice\": 2599, \n" +
                "      \"skuQty\": 100\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
