package com.example.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.vo.Constants;
import com.example.kafka.vo.UpdateRequest;
import com.example.kafka.vo.UpdateResponse;
import com.example.kafka.vo.Vo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author chenyh-a
 * @version created: 2022-11-26 20:43
 * 
 */
@RestController
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private KafkaTemplate<String, String> kt;

	@RequestMapping(value = "/send")
	public String send(@RequestBody UpdateRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		UpdateResponse rsp = new UpdateResponse();
		Vo order = request.data.get(0);
		String str = "";
		try {
			String sorder = mapper.writeValueAsString(order);
			kt.send(Constants.TOPIC_ORDER, "key", sorder);
			rsp.message = sorder;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			rsp.result = Constants.RESULT_FAIL;
			rsp.message = e.getMessage();
		}
		try {
			str = mapper.writeValueAsString(rsp);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return str;
	}
}
