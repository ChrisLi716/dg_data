package com.chris.dg_data.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("/rmq")
public class MqController {

	private Logger log = LoggerFactory.getLogger(MqController.class);

	@Resource
	DefaultMQProducer producer;

	@GetMapping(value = "/produdermsg")
	public void sendMsg(
		@RequestParam("input")
			String input)
		throws MQClientException, RemotingException, MQBrokerException, InterruptedException, UnsupportedEncodingException {
		String key = UUID.randomUUID().toString().replaceAll("-", "");
		Message msg = new Message("threezto-test", "tags1", key, input.getBytes(RemotingHelper.DEFAULT_CHARSET));

		// 发送消息到一个Broker
		SendResult sendResult = producer.send(msg);

		// 通过sendResult返回消息是否成功送达
		log.info("result:{}", sendResult.getSendStatus());
	}

}
