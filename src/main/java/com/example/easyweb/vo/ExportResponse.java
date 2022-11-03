package com.example.easyweb.vo;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class ExportResponse extends BaseResponse {

	public String fileUrl;// file URL for download, also recorded importing error info.
	public Integer totalNum; // total number of excel rows
	public Integer successNum;// total number of rows exported successfully
}