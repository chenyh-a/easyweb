package com.example.easyweb.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.easyweb.Constants;
import com.example.easyweb.ExcelUtil;
import com.example.easyweb.vo.ExportRequest;
import com.example.easyweb.vo.ExportResponse;
import com.example.easyweb.vo.ProcedureColumn;

/**
 * DB utilities
 * 
 * @author chenyh
 *
 */
@Component
public class ExportDao extends BaseDao<ExportRequest, ExportResponse> {

	private static Logger log = LoggerFactory.getLogger(ExportDao.class);

	@Override
	public ExportResponse execute(ExportRequest req0) {

		ExportRequest req = (ExportRequest) req0;
		ExportResponse rsp = req.copy();
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getStatement(req.method);
			for (ProcedureColumn pc : spCols) {
				// SP parameter name with prefix p_
				String spParamName = pc.columnName;
				// remove prefix p_ as DB column name
				String dbColName = spParamName.substring(2);

				Object val = req.data.get(dbColName);
				// 1 In 2 InOut 3 Out 4 Return
				if (pc.columnType == 1 || pc.columnType == 2) {
					stmt.setObject(pc.pos, val);
				}
			}
			rs = stmt.executeQuery();
			ExcelUtil.export(rs, req, rsp);

		} catch (Exception e) {
			rsp.result = Constants.RESULT_FAIL;
			rsp.message = e.getMessage();
			log.error(e.getMessage(), e);
		} finally {
			close(rs);
			close(stmt);
		}
		return rsp;
	}
}