package com.example.easyweb.vo;

/**
 * User request parameters passed from front end. params passed from front end
 * will use a JQuery dataTables JS component
 * 
 * @author chenyh
 *
 */
public class QueryRequest extends BaseRequest {
	/** required when query, get it from request */
	public Integer draw = 1;
	/** default 0, required when query */
	public Integer start;
	/** page size, required when query */
	public Integer length = 10;
	/** order DB column */
	public String orderColumn;
	/** order direction: ASC/DESC */
	public String orderDir;
	/** search criteria */
	public Vo data = new Vo();

	@Override
	public QueryResponse copy() {
		QueryResponse rsp = new QueryResponse();
		super.baseCopy(rsp);
		rsp.draw = this.draw;
		rsp.start = this.start;
		rsp.length = this.length;
		return rsp;
	}
}