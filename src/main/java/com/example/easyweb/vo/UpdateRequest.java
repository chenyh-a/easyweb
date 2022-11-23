package com.example.easyweb.vo;

import java.util.List;

/**
 * A common VO that contains user request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class UpdateRequest extends BaseRequest {

	/**
	 * parameter passed from UI page, if it is a query, user can pass more than one set of data records.
	 * Vo is a map contains all required data for manipulating later
	 */
	public List<Vo> data;

	@Override
	public UpdateResponse copy() {
		UpdateResponse rsp = new UpdateResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}