package com.example.easyweb.vo;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class ExportRequest extends BaseRequest {
	/** export file name without suffix */
	public String filename;
	/** current absolute directory easyweb/ */
	public String currRootDir;
	/** need auto sizing excel column width */
	public Boolean autoSizeColumn;
	/** print user code as water mark */
	public Boolean watermark;
	/** table column name and caption, required when import/export. */
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