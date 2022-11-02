package com.example.easyweb.vo;

/**
 * Stored procedure column
 * 
 * @author chenyh
 *
 */
public class ProcedureColumn {
	public String PROCEDURE_CAT;
	public String PROCEDURE_SCHEM;
	public String PROCEDURE_NAME;
	public String COLUMN_NAME; // SP parameter name should start with prefix p_
	public Short COLUMN_TYPE; // 0~5
	/**
	 * procedureColumnUnknown - <br/>
	 * procedureColumnIn - IN <br/>
	 * procedureColumnInOut - INOUT <br/>
	 * procedureColumnOut - OUT <br/>
	 * procedureColumnReturn - Return <br/>
	 * procedureColumnResult - ResultSet
	 */

	public Integer DATA_TYPE;// java.sql.Types
	public String TYPE_NAME;
	public Integer PRECISION;
	public Integer LENGTH;
	public Short SCALE;
	public Short RADIX;
	public Short NULLABLE;// 0~2
	/**
	 * procedureNoNulls <br/>
	 * procedureNullable <br/>
	 * procedureNullableUnknown
	 */

	public String REMARKS;//
	public Integer pos;// SP parameter position
	public String excelHeader;// use it when import
	public Integer excelIndex;// use it when import
}