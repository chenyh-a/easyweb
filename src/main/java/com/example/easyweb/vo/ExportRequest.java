package com.example.easyweb.vo;

/**
 * A common exporting VO that contains user request parameters passed from front end.
 * 
 * @author chenyh-a
 *
 */
public class ExportRequest extends BaseRequest {
	/** export file name without suffix */
	public String filename;

	/**
	 * current absolute directory of the web context(e.g. easyweb/) this parameter
	 * is not passed from front-end, it will be filled in controller
	 */
	public String currRootDir;

	/** need auto sizing column width in result excel file */
	public Boolean autoSizeColumn;

	/** print user code as water mark */
	public Boolean watermark;

	/** table column name and caption, */
	public Vs cols;

	/** extra info, usually searching criteria */
	public Vo data = new Vo();

	@Override
	public ExportResponse copy() {
		ExportResponse rsp = new ExportResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}