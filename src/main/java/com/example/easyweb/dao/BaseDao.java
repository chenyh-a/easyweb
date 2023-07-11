package com.example.easyweb.dao;

import com.example.easyweb.vo.ProcedureColumn;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseDao , initiate statement, close resource etc.
 * 
 * @author chenyh
 *
 */
public abstract class BaseDao<T, E> implements IDao<T, E> {

	/* static final String JNDI_NAME = "java:comp/env/jdbc/mariaDB";*/
	
	static final String PARAM_TOKEN = "p_token";

	@Resource
	DataSource dataSource;

	protected List<ProcedureColumn> spCols;

	private static Logger log = LoggerFactory.getLogger(BaseDao.class);

	protected CallableStatement getStatement(String method) throws Exception {
		if (method == null) {
			// define your own exception here.
			throw new Exception("Error: Method missing.");
		}
		// Context ctx = new InitialContext();
		// DataSource dataSource = (DataSource) ctx.lookup(JNDI_NAME);

		// use spring data source
		Connection conn = dataSource.getConnection();
		spCols = getSpParams(conn, method);
		String sql = "{call " + method + "(";

		for (int i = 0; i < spCols.size(); i++) {
			sql += "?";
			if (i < spCols.size() - 1) {
				sql += ",";
			}
		}
		sql += ")}";

		CallableStatement stmt = conn.prepareCall(sql);

		log.debug(sql);
		return stmt;
	}

	/**
	 * Get all parameters of specified stored procedure.
	 * 
	 * @param conn DB connection
	 * @param sp   Stored procedure name, All SP parameters must start with prefix
	 *             p_
	 * @return List of SP parameters.
	 */
	List<ProcedureColumn> getSpParams(Connection conn, String sp) throws Exception {
		List<ProcedureColumn> list = new ArrayList<>();
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet rs0 = dmd.getProcedureColumns(null, null, sp, null);
		int pos = 0;
		while (rs0.next()) {
			pos++;
			ProcedureColumn pc = new ProcedureColumn();
			String colName = rs0.getString("COLUMN_NAME");
			pc.columnName = colName;
			pc.columnType = rs0.getShort("COLUMN_TYPE");
			pc.dataType = rs0.getInt("DATA_TYPE");
			if (!colName.startsWith("p_")) {
				String ss = "Error: SP parameter not start with prefix p_: " + colName;
				log.error(ss);
				throw new Exception(ss);
			}
			pc.pos = pos;
			list.add(pc);
		}
		return list;
	}

	protected void close(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}

		} catch (Exception e) {
		}
	}

	protected void close(Statement stmt) {
		try {

			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
				if (!stmt.getConnection().isClosed()) {
					stmt.getConnection().close();
				}
			}
		} catch (Exception e) {
		}
	}
}