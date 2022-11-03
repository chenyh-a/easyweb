package com.example.easyweb.vo;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class ExportRequest extends BaseRequest {

	public String filename;// export file name without suffix
	public String currRootDir;// current absolute directory easyweb/
	public Boolean autoSizeColumn;// need auto sizing excel column width
	public Boolean watermark;// print user code as water mark
	public VOS cols;// table column name and caption, required when import/export.

	public VO data = new VO();//extra info, usually searching criteria

	public ExportResponse copy() {
		ExportResponse rsp = new ExportResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}