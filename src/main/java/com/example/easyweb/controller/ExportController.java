package com.example.easyweb.controller;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.easyweb.C;
import com.example.easyweb.dao.ExportDao;
import com.example.easyweb.vo.ExportRequest;
import com.example.easyweb.vo.ExportResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author chenyh-a
 *
 */
@RestController
public class ExportController {

	private static Logger log = LoggerFactory.getLogger(ExportController.class);

	@Autowired
	private ExportDao exportDao;

	@Autowired
	private ServletContext servletContext;

	@PostMapping(value = "/exportserver")
	public String exportPost(@RequestBody ExportRequest req) {
		long t0 = System.currentTimeMillis();
		ExportResponse rsp = new ExportResponse();
		String str = "";
		try {
			if (!C.RESULT_FAIL.equals(rsp.result)) {
				rsp = req.copy();
				req.currRootDir = servletContext.getRealPath("/");
				rsp = exportDao.execute(req);
				long t1 = System.currentTimeMillis();
				rsp.consumed = t1 - t0;
				ObjectMapper mapper = new ObjectMapper();
				str = mapper.writeValueAsString(rsp);
				log.debug(str);
			}
		} catch (Exception e) {
			rsp.result = C.RESULT_FAIL;
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
