package com.example.easyweb.vo;

import java.util.List;

public class DataTableRequest {
	public Integer draw;
	public Integer start;
	public Integer length;
	public List<VOS> columns;
	public List<VOS> order;

}
