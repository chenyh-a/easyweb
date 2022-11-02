package com.example.easyweb.vo;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class ImportResponse extends BaseResponse {

	public String fileUrl;// import error file.
	public String sourceFile;// return when import
	public Integer totalNum; // return when import
	public Integer successNum;// return when import
	public Integer errorNum;// return when import
}