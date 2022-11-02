package com.example.easyweb.vo;

/**
 * User request parameters passed from front end.
 * 
 * @author chenyh
 *
 */
public class ExportRequest extends BaseRequest {

	public String filename;// export file name without suffix
	public String currRootDir;// current absolute dir. simpleweb/
	public Boolean autoSizeColumn;//
	public Boolean watermark;//
	public VOS cols;// table column name and caption, required when import/export.

	public VO data = new VO();

	public ExportResponse copy() {
		ExportResponse rsp = new ExportResponse();
		super.baseCopy(rsp);
		return rsp;
	}
}