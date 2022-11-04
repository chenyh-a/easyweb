package com.example.easyweb.vo;

/**
 * Stored procedure column
 * 
 * @author chenyh
 *
 */
public class ProcedureColumn {
	public String procedureCat;
	public String procedureSchem;
	public String procedureName;
	/** SP parameter name should start with prefix p_*/
	public String columnName; 
	/** 0~5*/
	public Short columnType; 
	/**
	 * procedureColumnUnknown - <br/>
	 * procedureColumnIn - IN <br/>
	 * procedureColumnInOut - INOUT <br/>
	 * procedureColumnOut - OUT <br/>
	 * procedureColumnReturn - Return <br/>
	 * procedureColumnResult - ResultSet
	 */
	/** java.sql.Types*/
	public Integer dataType;
	public String typeName;
	public Integer precision;
	public Integer length;
	public Short scale;
	public Short radix;
	/** 0~2*/
	public Short nullable;
	/**
	 * procedureNoNulls <br/>
	 * procedureNullable <br/>
	 * procedureNullableUnknown
	 */

	public String remarks;
	/** SP parameter position*/
	public Integer pos = 0;
	/** mapping excel header name, use it when import*/
	public String excelHeader;
	/** mapping excel header index, use it when import*/
	public Integer excelIndex = 0;
}