package com.example.easyweb.vo;

import java.util.List;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class QueryResponse extends BaseResponse {

	public Integer draw;// jquery datatable passed
	public Integer start; // jquery datatable passed
	public Integer length; // jquery datatable passed
	public Integer recordsTotal; // return to jquery datatable
	public Integer recordsFiltered;// return to jquery datatable
	public String error;
	public List<VO> data;// return actual query data
}