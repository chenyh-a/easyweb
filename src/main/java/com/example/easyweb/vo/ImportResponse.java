package com.example.easyweb.vo;

/**
 * server side response that will be return to front end.
 * 
 * @author chenyh
 *
 */
public class ImportResponse extends BaseResponse {
	/** importing error file URL. */
	public String fileUrl;
	/** source file name */
	public String sourceFile;
	/** total rows of excel file */
	public Integer totalNum = 0;
	/** total rows imported successfully */
	public Integer successNum = 0;
	/** total error rows */
	public Integer errorNum = 0;
}