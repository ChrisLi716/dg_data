package com.chris.dg_data.rmq;

import lombok.Data;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;

@Data
public abstract class AbstractConsumer implements Consumer {

	private String topics;

	private String tags;

	private MessageListener messageListener;

	private String consumerTitel;

	MQPushConsumer mqPushConsumer;

	/**
	 * 必要的信息
	 *
	 * @param topics
	 * @param tags
	 * @param consumerTitel
	 */
	void necessary(String topics, String tags, String consumerTitel) {
		this.topics = topics;
		this.tags = tags;
		this.consumerTitel = consumerTitel;
	}

	public abstract void init();

	@Override
	public void registerMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}
}
