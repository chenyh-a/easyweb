package com.example.easyweb.vo;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
public class UploadResponse extends BaseResponse {

	public String originalFilename;
	public Long fileSize;
	/** destination file path */
	public String destFilePath;
}
