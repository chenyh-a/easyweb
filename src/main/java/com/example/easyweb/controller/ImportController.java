package com.example.easyweb.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.easyweb.Util;
import com.example.easyweb.dao.ImportDao;
import com.example.easyweb.vo.ImportRequest;
import com.example.easyweb.vo.ImportResponse;
import com.example.easyweb.vo.Vs;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
@RestController
public class ImportController {
	private static Logger log = LoggerFactory.getLogger(ImportController.class);

	@Autowired
	private ImportDao importDao;

	@RequestMapping(value = "/importserver")
	public String importPost(@RequestParam("file1") MultipartFile f1, 
			@RequestParam("method") String method,
			@RequestParam("verifyMethod") String verifyMethod, 
			@RequestParam("tag") String tag,
			@RequestParam("token") String token, 
			@RequestParam("cols") String cols,
			@RequestParam("userCode") String userCode, 
			@RequestParam("roleCode") String roleCode,
			HttpServletRequest request) throws IOException {
		long t0 = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();
		ImportRequest req = new ImportRequest();
		req.method = method;
		req.verifyMethod = verifyMethod;
		req.token = token;
		req.tag = tag;
		req.cols = mapper.readValue(cols, Vs.class);
		req.userCode = userCode;
		// optional
		req.roleCode = roleCode; 

		ImportResponse rsp = req.copy();

		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String currDir = request.getServletContext().getRealPath("/");
		String fileUrl = "upload/" + today + "/" + Util.getTempFileName(f1.getOriginalFilename());
		String destFilePath = currDir + fileUrl;
		log.debug("upload original file=" + f1.getOriginalFilename());
		log.debug("upload destFilePath=" + destFilePath);
		File f = new File(destFilePath);
		f.getParentFile().mkdirs();
		f1.transferTo(f);

		req.fullPath = destFilePath;

		rsp = importDao.execute(req);
		long t1 = System.currentTimeMillis();
		rsp.sourceFile = f1.getOriginalFilename();
		rsp.fileUrl = fileUrl;
		rsp.consumed = t1 - t0;
		String str = mapper.writeValueAsString(rsp);
		return str;
	}
}
