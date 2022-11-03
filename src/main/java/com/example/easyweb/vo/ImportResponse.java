package com.example.easyweb.vo;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class ImportResponse extends BaseResponse {

	public String fileUrl;// importing error file URL.
	public String sourceFile;// source file name
	public Integer totalNum; // total rows of excel file
	public Integer successNum;// total rows imported successfully
	public Integer errorNum;// total error rows
}