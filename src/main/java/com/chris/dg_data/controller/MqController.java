package com.chris.dg_data.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/rmq")
public class MqController {

	@Resource
	DefaultMQProducer producer;

	@GetMapping(value = "/produdermsg")
	public void qmtest(
		@RequestParam("input")
			String input)
		throws MQClientException, RemotingException, MQBrokerException, InterruptedException, UnsupportedEncodingException {
		Message msg = new Message("threezto-test", "tags1", input.getBytes(RemotingHelper.DEFAULT_CHARSET));

		// 发送消息到一个Broker
		SendResult sendResult = producer.send(msg);

		// 通过sendResult返回消息是否成功送达
		System.out.printf("%s%n", sendResult);
	}

}
