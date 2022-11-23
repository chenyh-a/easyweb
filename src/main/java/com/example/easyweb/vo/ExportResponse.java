package com.example.easyweb.vo;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class ExportResponse extends BaseResponse {
	/** file URL for download, also recorded importing error info. */
	public String fileUrl;
	
	/** total number of excel rows */
	public Integer totalNum;
	
	/** total number of rows exported successfully */
	public Integer successNum;
}