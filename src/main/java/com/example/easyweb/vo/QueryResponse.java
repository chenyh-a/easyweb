package com.example.easyweb.vo;

import java.util.List;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class QueryResponse extends BaseResponse {

	public Integer draw;// unique post id, JQuery dataTables passed
	public Integer start; // skipped record number, JQuery dataTables passed
	public Integer length; // fetch record number, JQuery dataTables passed
	public Integer recordsTotal; // return to JQuery dataTables
	public Integer recordsFiltered;// return to JQuery dataTables
	public String error;
	public List<VO> data;// return actual query data
}