package com.example.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author chenyh-a
 * @version created: 2022-11-26 12:00
 * 
 */
@Component
public class OrderConsumer {
	private static Logger log = LoggerFactory.getLogger(OrderConsumer.class);

	@KafkaListener(topics = { "TOPIC_ORDER" }, groupId = "GROUP_LOGISTICS")
	public void consume(String message) {
		// put actual consumer code here.
		log.info("Consumer receive: " + message);
	}
}
