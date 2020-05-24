package com.chris.dg_data.rmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultConsumerMQ extends AbstractConsumer {

	private Logger log = LoggerFactory.getLogger(DefaultConsumerMQ.class);

	/**
	 * 初始化消费者
	 */
	@Override
	public void init() {
		// 设置主题,标签与消费者标题
		super.necessary("threezto-test", "*", "这是标题");

		//消费者具体执行逻辑
		registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				msgs.forEach(msg -> {
					log.info("consumer message body {}", new String(msg.getBody()));
				});

				// 标记该消息已经被成功消费
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
	}
}
