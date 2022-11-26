package com.example.kafka;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author chenyh-a
 * @version created: 2022-11-26 20:50
 * 
 */
@Component
public class OrderConsumer {
	@KafkaListener(topics = { "TOPIC_ORDER" }, groupId = "GROUP_LOGISTICS")
	public void onMessage(String message) {
		System.out.println("Order consumer received:" + message);
		//write actual consumer code here
	}
}
