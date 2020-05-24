package com.chris.dg_data.rmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * mq配置
 */

@Configuration
@EnableConfigurationProperties({RocketMQProperties.class})
@ConditionalOnProperty(prefix = "rocketmq",
					   value = "isEnable",
					   havingValue = "true")
public class RocketMQConfiguation {
	private Logger log = LoggerFactory.getLogger(RocketMQConfiguation.class);

	private RocketMQProperties properties;

	private ApplicationContext applicationContext;

	public RocketMQConfiguation(RocketMQProperties properties, ApplicationContext applicationContext) {
		this.properties = properties;
		this.applicationContext = applicationContext;
	}

	/**
	 * 注入一个默认的生产者
	 *
	 * @return DefaultMQProducer
	 * @throws MQClientException mq client exception
	 */
	@Bean(name = "producer")
	public DefaultMQProducer getRocketMQProducer()
		throws MQClientException {
		if (StringUtils.isEmpty(properties.getGroupName())) {
			throw new MQClientException(-1, "groupName is blank");
		}

		if (StringUtils.isEmpty(properties.getNamesrvAddr())) {
			throw new MQClientException(-1, "nameServerAddr is blank");
		}

		DefaultMQProducer producer = new DefaultMQProducer(properties.getGroupName());
		producer.setNamesrvAddr(properties.getNamesrvAddr());
		// producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

		// 如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
		// producer.setInstanceName(instanceName);

		producer.setMaxMessageSize(properties.getProducerMaxMessageSize());
		producer.setSendMsgTimeout(properties.getProducerSendMsgTimeout());

		// 如果发送消息失败，设置重试次数，默认为2次
		producer.setRetryTimesWhenSendFailed(properties.getProducerRetryTimesWhenSendFailed());

		try {
			producer.start();
			log.info("producer is start ! groupName:{},namesrvAddr:{}", properties.getGroupName(), properties.getNamesrvAddr());
		}
		catch (MQClientException e) {
			log.error("producer is error {}", e.getMessage(), e);
			throw e;
		}
		return producer;
	}

	/**
	 * 通过消费者信息创建消费者
	 *
	 * @param consumerPojo
	 */
	public void createConsumer(AbstractConsumer consumerPojo) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.properties.getGroupName());
		consumer.setNamesrvAddr(this.properties.getNamesrvAddr());
		consumer.setConsumeThreadMin(this.properties.getConsumerConsumeThreadMin());
		consumer.setConsumeThreadMax(this.properties.getConsumerConsumeThreadMax());
		consumer.registerMessageListener(consumerPojo.getMessageListener());

		// 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
		// consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		// 设置消费模型，集群还是广播，默认为集群
		// consumer.setMessageModel(MessageModel.CLUSTERING);

		// 设置一次消费消息的条数，默认为1条
		consumer.setConsumeMessageBatchMaxSize(this.properties.getConsumerConsumeMessageBatchMaxSize());
		try {
			consumer.subscribe(consumerPojo.getTopics(), consumerPojo.getTags());
			consumer.start();
			consumerPojo.mqPushConsumer = consumer;
		}
		catch (MQClientException e) {
			log.error("info consumer title {}", consumerPojo.getConsumerTitel(), e);
		}

	}

	/**
	 * SpringBoot启动时加载所有消费者
	 */
	@PostConstruct
	public void initConsumer() {
		Map<String, AbstractConsumer> consumers = applicationContext.getBeansOfType(AbstractConsumer.class);
		log.info("load all consumer, sum: {}", consumers.size());
		if (CollectionUtils.isEmpty(consumers)) {
			log.info("init rocket consumer 0");
			return;
		}
		for (String beanName : consumers.keySet()) {
			AbstractConsumer consumer = consumers.get(beanName);
			consumer.init();
			createConsumer(consumer);

			log.info("init success consumer title {} , toips {} , tags {}",
				consumer.getConsumerTitel(),
				consumer.getTags(),
				consumer.getTopics());
		}
	}

}