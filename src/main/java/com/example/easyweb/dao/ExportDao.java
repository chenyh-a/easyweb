package com.example.easyweb.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.easyweb.C;
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
		
		try {
			init(req.method);
			for (ProcedureColumn pc : spCols) {
				String spParamName = pc.COLUMN_NAME;// SP parameter name with prefix p_

				String dbColName = spParamName.substring(2);// remove prefix p_ as DB column name

				Object val = req.data.get(dbColName);

				if (pc.COLUMN_TYPE == 1 || pc.COLUMN_TYPE == 2) {// 1 In 2 InOut 3 Out 4 Return
					stmt.setObject(pc.pos, val);
				}
			}
			rs = stmt.executeQuery();
			ExcelUtil.export(rs, req, rsp);

		} catch (Exception e) {
			rsp.result = C.RESULT_FAIL;
			rsp.message = e.getMessage();
			log.error(e.getMessage(), e);
		} finally {
			close();
		}
		return rsp;
	}
}