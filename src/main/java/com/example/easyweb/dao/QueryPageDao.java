package com.example.easyweb.dao;

import com.example.easyweb.Constants;
import com.example.easyweb.Util;
import com.example.easyweb.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * DB utilities<br/>
 * If you query data list, you must define 4 parameters below in your SP:<br/>
 * p_get_offset -- start to query skipped record number<br/>
 * p_get_num -- page size<br/>
 * p_order_column -- order by DB column<br/>
 * p_order_type -- DESC/ASC<br/>
 * p_total_records -- output parameter <br/>
 * 
 * @author chenyh
 *
 */
@Component
public class QueryPageDao extends BaseDao<QueryPageRequest, QueryPageResponse> {
	/** skip record */
	static final String PARAM_GET_OFFSET = "p_get_offset";
	/** page size */
	static final String PARAM_GET_NUM = "p_get_num";
	/** order by column */
	static final String PARAM_ORDER_COLUMN = "p_order_column";
	/** DESC / ASC */
	static final String PARAM_ORDER_DIR = "p_order_dir";
	/** Should define this output parameter in you list SP */
	static final String PARAM_TOTAL_RECORDS = "p_total_records";
	/** login user code */
	static final String PARAM_USER_CODE = "p_usercode";

	private static Logger log = LoggerFactory.getLogger(QueryPageDao.class);

	@Override
	public QueryPageResponse execute(QueryPageRequest req) {
		QueryPageResponse rsp = req.copy();
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getStatement(req.method);
			int totalPos = 0;
			for (ProcedureColumn pc : spCols) {
				// SP parameter name with prefix p_
				String spParamName = pc.columnName;
				Object val = null;
				// remove prefix p_ as DB column name
				String dbColName = spParamName.substring(2);
				if (PARAM_GET_NUM.equals(spParamName)) {
					val = req.length;
				} else if (PARAM_GET_OFFSET.equals(spParamName)) {
					val = req.start;
				} else if (PARAM_ORDER_COLUMN.equals(spParamName)) {
					val = req.orderColumn;
				} else if (PARAM_ORDER_DIR.equals(spParamName)) {
					val = req.orderDir;
				} else if (PARAM_USER_CODE.equals(spParamName)) {
					val = req.userCode;
				} else {
					val = req.criteria.get(dbColName);
				}
				// 1 In 2 InOut 3 Out 4 Return
				if (pc.columnType == 1 || pc.columnType == 2) {
					stmt.setObject(pc.pos, val);
				}
				// register out parameter
				if (pc.columnType == 2 || pc.columnType == 3 || pc.columnType == 4) {
					stmt.registerOutParameter(pc.pos, pc.dataType);
					if (PARAM_TOTAL_RECORDS.equals(spParamName)) {
						totalPos = pc.pos;
					}
				}
			}
			rs = stmt.executeQuery();
			if (totalPos > 0) {
				rsp.recordsTotal = stmt.getInt(totalPos);
			}
			rsp.data = Util.getDataFromResultSet(rs);
			rsp.result = Constants.RESULT_SUCCESS;
		} catch (Exception e) {
			rsp.result = Constants.RESULT_FAIL;
			rsp.error = e.getMessage();
			log.error(e.getMessage(), e);
		} finally {
			close(rs);
			close(stmt);
		}
		return rsp;
	}

}