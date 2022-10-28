package com.example.easyweb.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.easyweb.vo.ProcedureColumn;

/**
 * BaseDao , initiate statement, close resource etc.
 * 
 * @author chenyh
 *
 */
public abstract class BaseDao<T, E> implements IDao<T, E> {

	// static final String JNDI_NAME = "java:comp/env/jdbc/mariaDB";
	static final String PARAM_TOKEN = "p_token";

	@Autowired
	DataSource dataSource;

	protected Connection conn;
	protected CallableStatement stmt;
	protected List<ProcedureColumn> spCols;
	protected ResultSet rs;

	private static Logger log = LoggerFactory.getLogger(BaseDao.class);

	public abstract E execute(T req);

	protected void init(String method) throws Exception {
		if (method == null) {
			throw new Exception("Error: Method missing.");// define your own exception here.
		}
		// Context ctx = new InitialContext();
		// DataSource dataSource = (DataSource) ctx.lookup(JNDI_NAME);

		conn = dataSource.getConnection();// use spring data source
		spCols = getSpParams(conn, method);
		String sql = "{call " + method + "(";

		for (int i = 0; i < spCols.size(); i++) {
			sql += "?";
			if (i < spCols.size() - 1) {
				sql += ",";
			}
		}
		sql += ")}";

		stmt = conn.prepareCall(sql);

		log.debug(sql);

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
			pc.COLUMN_NAME = colName;
			pc.COLUMN_TYPE = rs0.getShort("COLUMN_TYPE");
			pc.DATA_TYPE = rs0.getInt("DATA_TYPE");
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

	protected void close() {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (stmt != null && !stmt.isClosed())
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				//conn.close();
			}
		} catch (Exception e) {
		}
	}

}