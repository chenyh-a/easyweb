package com.example.easyweb.dao;

import java.sql.CallableStatement;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.easyweb.Constants;
import com.example.easyweb.vo.ProcedureColumn;
import com.example.easyweb.vo.UpdateRequest;
import com.example.easyweb.vo.UpdateResponse;
import com.example.easyweb.vo.Vo;

/**
 * DB utilities
 * 
 * @author chenyh
 *
 */
@Component
public class UpdateDao extends BaseDao<UpdateRequest, UpdateResponse> {

	private static Logger log = LoggerFactory.getLogger(QueryDao.class);

	@Override
	public UpdateResponse execute(UpdateRequest req0) {

		UpdateRequest req = (UpdateRequest) req0;
		UpdateResponse rsp = req.copy();
		if (req.data == null || req.data.size() == 0) {
			String s = "Error: missing update contents,req.data=" + req.data;
			log.error(s);
			rsp.result = "FAILED";
			rsp.message = s;
			return rsp;
		}
		CallableStatement stmt = null;

		try {
			stmt = getStatement(req.method);
			for (Vo vo : req.data) {
				for (ProcedureColumn pc : spCols) {
					// SP parameter name, should start with prefix p_
					String spParamName = pc.columnName;
					Object val = null;
					// remove prefix p_ as a DB column name
					String dbColName = spParamName.substring(2);
					val = vo.get(dbColName);
					int type = pc.dataType;

					if ("p_usercode".equals(spParamName)) {
						stmt.setObject(pc.pos, req.userCode);

					} else if (pc.columnType == 1 || pc.columnType == 2) {
						// 1 In 2 InOut 3 Out 4 Return
						if (isNumeric(type) && "".equals(val)) {
							val = 0;
						}
						stmt.setObject(pc.pos, val);
					}
					if (pc.columnType == 2 || pc.columnType == 3 || pc.columnType == 4) {
						// register out parameter
						stmt.registerOutParameter(pc.pos, pc.dataType);
					}
				}

				try {
					rsp.affected += stmt.executeUpdate();
					rsp.result = Constants.RESULT_SUCCESS;
				} catch (Exception e) {
					rsp.result = Constants.RESULT_FAIL;
					rsp.message = e.getMessage();
					log.error("Error occured. request row data: " + vo, e);
					close(stmt);
					throw e;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			close(stmt);
		}
		return rsp;
	}

	boolean isNumeric(int type) {
		return type == Types.INTEGER || type == Types.BIGINT || type == Types.DECIMAL || type == Types.DOUBLE
				|| type == Types.FLOAT || type == Types.NUMERIC || type == Types.REAL;
	}
}