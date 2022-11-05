package com.example.easyweb.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.easyweb.Constants;
import com.example.easyweb.dao.QueryDao;
import com.example.easyweb.vo.QueryRequest;
import com.example.easyweb.vo.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
@RestController
public class QueryController {
	private static Logger log = LoggerFactory.getLogger(QueryController.class);
	/** unique number passed from front end dataTables */
	private static final String DRAW = "draw";

	@Autowired
	private QueryDao queryDao;

	@GetMapping(value = "/queryget")
	public String queryGet(@RequestParam Map<String, String> params) {
		QueryResponse rsp = new QueryResponse();
		QueryRequest req = new QueryRequest();
		String str = "";
		try {
			if (params.containsKey(DRAW)) {
				req.draw = Integer.valueOf(params.get("draw").toString());
				req.start = Integer.valueOf(params.get("start").toString());
				req.length = Integer.valueOf(params.get("length").toString());
			}
			req.userCode = params.get("userCode");
			String iorder = params.get("order[0][column]");
			if (iorder != null) {
				req.orderColumn = params.get("columns[" + iorder + "][data]");
				req.orderDir = params.get("order[0][dir]");
			}
			String search = params.get("search[value]");
			req.data.put("search", search);
			for (String key : params.keySet()) {
				String val = params.get(key);
				// System.out.println("---param:" + key + "=" + params.get(key));
				if (key.startsWith("data[")) {
					int len = key.length();
					String key0 = key.substring(5, len - 1);
					req.data.put(key0, val);
				}
			}
			req.method = params.get("method");

			if (!Constants.RESULT_FAIL.equals(rsp.result)) {
				rsp = req.copy();
				rsp = queryDao.execute(req);
				rsp.recordsFiltered = rsp.recordsTotal;
			}
		} catch (Exception e) {
			rsp.result = Constants.RESULT_FAIL;
			log.error(e.getMessage(), e);
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(rsp);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug(str);
		return str;
	}

	@PostMapping(value = "/querypost")
	public String queryPost(@RequestBody QueryRequest req) {
		QueryResponse rsp = new QueryResponse();
		String str = "";
		try {
			if (!Constants.RESULT_FAIL.equals(rsp.result)) {
				rsp = req.copy();
				rsp = queryDao.execute(req);
			}
		} catch (Exception e) {
			rsp.result = Constants.RESULT_FAIL;
			log.error(e.getMessage(), e);
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(rsp);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug(str);
		return str;
	}
}