package com.example.easyweb.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class QueryResponse extends BaseResponse {


	public String error;

	/** return actual query data */
	public List<Vo> data = new ArrayList<>();
}