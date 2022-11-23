package com.example.easyweb.vo;

/**
 * A common importing VO that contains user request parameters passed from front
 * end.
 * 
 * @author chenyh
 *
 */
public class ImportRequest extends BaseRequest {
	/** unique id for importing this time */
	public String token;

	/**
	 * full path of actual uploaded file this parameter is not passed from
	 * front-end, it will be filled in controller
	 */
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