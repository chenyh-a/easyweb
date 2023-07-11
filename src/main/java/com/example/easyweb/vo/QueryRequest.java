package com.example.easyweb.vo;

/**
 * A common VO that contains user request parameters passed from front end.
 * it will accept searching criteria from JQuery dataTables JS component
 * 
 * @author chenyh
 *
 */
public class QueryRequest extends BaseRequest {

	/** order DB column */
	public String orderColumn;
	/** order direction: ASC/DESC */
	public String orderDir;
	/** search criteria */
	public Vo criteria = new Vo();

	@Override
	public QueryResponse copy() {
		QueryResponse rsp = new QueryResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}