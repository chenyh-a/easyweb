package com.example.easyweb.vo;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class QueryRequest extends BaseRequest {

	public Integer draw = 1;// required when query, get it from request
	public Integer start; // default 0, required when query
	public Integer length = 10; // page size, required when query
	public String orderColumn;// order DB column
	public String orderDir;// order direction: ASC/DESC
	public VO data = new VO();// search criteria

	public QueryResponse copy() {
		QueryResponse rsp = new QueryResponse();
		super.baseCopy(rsp);
		rsp.draw = this.draw;
		rsp.start = this.start;
		rsp.length = this.length;
		return rsp;
	}
}