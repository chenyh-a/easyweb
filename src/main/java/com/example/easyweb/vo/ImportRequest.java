package com.example.easyweb.vo;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class ImportRequest extends BaseRequest {
	/** required when import */
	public String token;
	/** full path of actual uploaded file */
	public String fullPath;
	/** verify SP name */
	public String verifyMethod;
	/** table column name and caption, required when import/export. */
	public Vs cols;

	@Override
	public ImportResponse copy() {
		ImportResponse rsp = new ImportResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}