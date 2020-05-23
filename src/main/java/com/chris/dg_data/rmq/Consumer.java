package com.chris.dg_data.rmq;

import org.apache.rocketmq.client.consumer.listener.MessageListener;

public interface Consumer {

	/**
	 * 初始化消费者
	 */
	void init();

	/**
	 * 注册监听
	 *
	 * @param messageListener message listener
	 */
	void registerMessageListener(MessageListener messageListener);
}
