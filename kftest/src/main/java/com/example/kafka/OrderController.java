package com.example.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order producer sample
 * @author chenyh-a
 * @version created: 2022-11-26 11:52
 * 
 */

@RestController
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@RequestMapping(value = "/send")
	public String sendOrder(@RequestParam Map<String, String> params) {
		
		// change it to data collected from front-end
		String sorder = "{\"orderId\":1234,\"memberId\":\"2345\",\"productId\":\"3456\"}";
		
		kafkaTemplate.send("TOPIC_ORDER", "key", sorder);
		
		log.debug("Kafka send order success.");
		
		return "{\"result\":\"SUCCESS\"}";
	}

}
