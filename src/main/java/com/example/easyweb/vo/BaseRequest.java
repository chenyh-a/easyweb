package com.example.easyweb.vo;

import java.util.List;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public abstract class BaseRequest {

	public String method;
	public String userCode;
	public String roleCode;
	public String tag;

	/**
	 * parameter passed from UI page, if it is a query,pass only one set parameter
	 * as query condition, if update, user can pass more than one set of data.
	 */
	public List<Vo> data;

	/**
	 * copy some properties from request to response
	 * @return  initial response object
	 */
	public abstract BaseResponse copy();

	public void baseCopy(BaseResponse rsp) {
		rsp.method = this.method;
		rsp.tag = this.tag;
	}
}