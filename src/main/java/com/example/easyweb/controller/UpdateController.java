package com.example.easyweb.controller;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.easyweb.Constants;
import com.example.easyweb.dao.UpdateDao;
import com.example.easyweb.vo.UpdateRequest;
import com.example.easyweb.vo.UpdateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A common routine controller that can process create/update/delete, etc
 * you can implement specific business operation in you database SP
 * @author chenyh-a
 *
 */
@RestController
public class UpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(UpdateController.class);

	@Autowired
	private UpdateDao updateDao;

	/**
	 * @param req collected data passed from front-end
	 */
	@PostMapping(value = "/updateserver")
	public String updatePost(@RequestBody UpdateRequest req) {
		UpdateResponse rsp = new UpdateResponse();
		String str = "";
		try {
			if (!Constants.RESULT_FAIL.equals(rsp.result)) {
				rsp = req.copy();
				rsp = updateDao.execute(req);
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
