package com.example.easyweb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.easyweb.vo.BaseResponse;
import com.example.easyweb.vo.Vo;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
public class Util {
	private static final int DELAY = 7200 * 1000;
	private static final int MAX_ONCESTR_LEN = 10000;
	private static List<String> noncestrs = new ArrayList<>();
	private static Logger log = LoggerFactory.getLogger(Util.class);

	/**
	 * Real all InputStream content as a string
	 * 
	 * @param is
	 * @return All content of the input stream
	 */
	public static String getFromInputStream(InputStream is) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return sb.toString();
	}

	/**
	 * Get all data from a DB resultset
	 * 
	 * @param rs
	 * @return List filled with one or many DB rows as Map
	 * @throws Exception
	 */
	public static List<Vo> getDataFromResultSet(ResultSet rs) throws Exception {
		List<Vo> list = new ArrayList<>();
		if (rs != null) {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				int n = rsmd.getColumnCount();
				Vo obj = new Vo();
				for (int i = 1; i < n + 1; i++) {
					String colLabel = rsmd.getColumnLabel(i);
					int type = rsmd.getColumnType(i);
					Object val = rs.getObject(i);
					if (val == null) {
						val = "";
					} else if (type == Types.TIMESTAMP) {
						/* mysql datetime has tail .0 */
						val = val.toString().substring(0, 19);
					}
					obj.put(colLabel.toLowerCase(), val.toString());
				}
				list.add(obj);
			}
		}
		return list;
	}

	public static void checkRequestHeader(HttpServletRequest request, BaseResponse rsp) {
		String appkey = request.getHeader(Contants.APP_KEY);
		String timestamp = request.getHeader(Contants.TIMESTAMP);
		String noncestr = request.getHeader(Contants.NONCE_STR);
		String reqstr = request.getHeader(Contants.REQ_STR);

		String signtype = request.getHeader(Contants.SIGN_TYPE);
		String signature = request.getHeader(Contants.SIGNATURE);

		Map<String, Object> params = new TreeMap<>();
		params.put(Contants.APP_KEY, appkey);
		params.put(Contants.TIMESTAMP, timestamp);
		params.put(Contants.NONCE_STR, noncestr);
		params.put(Contants.REQ_STR, reqstr);

		long t0 = System.currentTimeMillis();
		long t1 = 0;
		try {
			t1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp).getTime();
		} catch (Exception e) {
			log.error("Timestamp empty or invalid.");
		}

		String serverSignature = getSignature(params, signtype);
		if ("".equals(reqstr)) {
			rsp.message = "Request data empty.";
		} else if (t1 == 0 || t1 - t0 > DELAY) {
			rsp.message = "Time verification failed.";
		} else if (noncestrs.contains(noncestr)) {
			rsp.message = "Duplicate nonce string.";
			// invalid signature
		} else if (!signature.equals(serverSignature)) {
			rsp.message = "Invalid signature, illegal access!";
		} else {
			synchronized (noncestrs) {
				noncestrs.add(noncestr);
				if (noncestrs.size() > MAX_ONCESTR_LEN) {
					noncestrs.remove(0);
				}
			}
		}
		if (rsp.message != null) {
			rsp.result = Contants.RESULT_FAIL;
		} else {
			rsp.result = Contants.RESULT_SUCCESS;
		}

	}

	/**
	 * server side signature
	 * 
	 * @param params
	 * @param signType
	 * @return
	 */
	private static String getSignature(Map<String, Object> params, String signType) {
		log.debug("params=" + params);
		String s1 = "";
		for (String key : params.keySet()) {
			s1 += key.toLowerCase() + params.get(key);
		}
		log.debug("s1=" + s1);
		String s2 = "";
		try {
			MessageDigest digest = MessageDigest.getInstance(signType);
			digest.update(s1.getBytes("UTF-8"));
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			s2 = hexString.toString().toLowerCase();
			log.debug("server signature=" + s2);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return s2;
	}

	public static String getTempFileName(String baseName) {
		Random random = new Random();
		// 生成一个8位
		int rannum = (int) (random.nextDouble() * (99999999 - 10000000 + 1)) + 10000000;
		int i = baseName.lastIndexOf('.');
		String ext = "";
		String fname = baseName;
		if (i > 0) {
			fname = baseName.substring(0, i);
			ext = baseName.substring(i);
		}
		return fname + "-" + rannum + ext;
	}
}
