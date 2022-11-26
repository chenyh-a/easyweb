package com.example.kafka;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenyh-a
 * @version created: 2022-11-26 20:43
 * 
 */
@RestController
public class OrderController {
	@Autowired
	private KafkaTemplate<String, String> kt;

	@RequestMapping(value = "/send")
	public String send(@RequestParam Map<String, String> params) {
		// change it to actual code, collect front-end data,etc.
		String sorder = "{\"orderId\":12345}";
		kt.send("TOPIC_ORDER", "key", sorder);
		return "{\"result\":\"SUCCESS\"}";
	}

}
