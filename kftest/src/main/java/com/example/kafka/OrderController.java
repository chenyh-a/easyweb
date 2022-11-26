package com.example.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenyh-a
 * @version created: 2022-11-26 11:52
 * 
 */

@RestController
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@GetMapping(value = "/send")
	public String sendOrder(@RequestParam Map<String, String> params) {
		String order = "{\"orderId\":12333,\"orderDate\":1234234234}";
		kafkaTemplate.send("TOPIC_ORDER", "key", order);
		log.debug("Kafka send order success.");
		return "Success";
	}

}
