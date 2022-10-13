package com.example.easyweb.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.easyweb.C;
import com.example.easyweb.U;
import com.example.easyweb.vo.UploadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UploadController {
	private static Logger log = LoggerFactory.getLogger(UploadController.class);

	@RequestMapping(value = "/uploadserver")
	public String uploadPost(@RequestParam("fileupload") MultipartFile f1, HttpServletRequest request) throws IOException {
		long t0 = System.currentTimeMillis();
		UploadResponse rsp = new UploadResponse();
		String s_today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String currDir = request.getServletContext().getRealPath("/");
		String destFilePath = currDir + "upload/" + s_today + "/" + U.getTempFileName(f1.getOriginalFilename());
		log.debug("upload original file=" + f1.getOriginalFilename());
		log.debug("upload destFilePath=" + destFilePath);
		File f = new File(destFilePath);
		f.getParentFile().mkdirs();
		f1.transferTo(f);
		long t1 = System.currentTimeMillis();
		rsp.result = C.RESULT_SUCCESS;
		rsp.consumed = t1 - t0;
		rsp.originalFilename = f1.getOriginalFilename();
		rsp.fileSize = f1.getSize();
		rsp.destFilePath = destFilePath;
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(rsp);
		return str;
	}
}
