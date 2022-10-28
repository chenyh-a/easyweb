package com.example.easyweb.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.easyweb.C;
import com.example.easyweb.U;
import com.example.easyweb.vo.ImportRequest;
import com.example.easyweb.vo.ImportResponse;
import com.example.easyweb.vo.ProcedureColumn;
import com.example.easyweb.vo.VO;
import com.example.easyweb.vo.VOS;

/**
 * DB utilities
 * 
 * @author chenyh
 *
 */
@Component
public class ImportDao extends BaseDao<ImportRequest, ImportResponse> {

	private static Logger log = LoggerFactory.getLogger(ImportDao.class);
	private static final String PARAM_ROWNUM = "p_rownum";

	/**
	 * Import a excel file, the first row of the excel file is column header.<br/>
	 * In Database, you should create 1 table and 2 store procedures<br/>
	 * xxxx_import (table) include 3 new column: token, rownum, error_message
	 * besides needed table column<br/>
	 * sp_imp_xxxx (SP) insert one row<br/>
	 * sp_imp_xxxx_verify(SP) verify data in xxxx_import and save error info in
	 * error_message column<br/>
	 * 
	 * @param req ImportRequest
	 * @return ImportResponse
	 */
	@Override
	public ImportResponse execute(ImportRequest req0) {

		ImportRequest req = (ImportRequest) req0;
		ImportResponse rsp = req.copy();
		CallableStatement stmt = null;
		// ResultSet rs = null;
		try {
			stmt = getStatement(req.method);
			Workbook wb = new XSSFWorkbook(new File(req.fullPath));
			Sheet sheet1 = wb.getSheetAt(0);
			Row row = sheet1.getRow(0);

			initExcelIndex(req.cols, row);
			for (ProcedureColumn pc : spCols) {
				String dbColName = pc.COLUMN_NAME.substring(2);
				if (!req.cols.containsKey(dbColName) && (!"token".equals(dbColName)) && (!"rownum".equals(dbColName))) {
					String ss = "Error: SP parameter not defined in front end,SP=" + req.method + ",dbColName= "
							+ pc.COLUMN_NAME;
					log.error(ss);
					throw new Exception(ss);
				}
			}
			int n = 0;
			stmt.getConnection().setAutoCommit(false);// Important!
			for (int i = 1; i <= sheet1.getLastRowNum() + 1; i++) {
				row = sheet1.getRow(i);
				if (row == null) {
					continue;
				}

				for (ProcedureColumn pc : spCols) {
					Object val = null;

					Cell cell = row.getCell(pc.excelIndex);
					CellType tp = cell.getCellType();
					String key = pc.COLUMN_NAME;
					if (key.equals(PARAM_TOKEN)) {
						val = req.token;
					} else if (key.equals(PARAM_ROWNUM)) {
						val = i;
					} else if (tp == CellType.NUMERIC) {
						val = cell.getNumericCellValue();
					} else if (tp == CellType.STRING) {
						val = cell.getStringCellValue().trim();
					}
					stmt.setObject(pc.pos, val);
				}
				stmt.addBatch();
				n++;
				if (i % 1000 == 0) {
					stmt.executeBatch();
					stmt.clearBatch();
					stmt.getConnection().commit();
					n = 0;
				}
			}
			if (n > 0) {
				stmt.executeBatch();
				stmt.getConnection().commit();
			}
			wb.close();
			stmt.getConnection().setAutoCommit(true);
			executeImportVerify(req, rsp);
		} catch (Exception e) {
			rsp.result = C.RESULT_FAIL;
			rsp.message = e.getMessage();
			log.error(e.getMessage(), e);
		}
		return rsp;
	}

	private void initExcelIndex(VOS cols, Row row) throws Exception {

		for (ProcedureColumn pc : spCols) {
			String key = pc.COLUMN_NAME;
			if (PARAM_TOKEN.equals(key) || PARAM_ROWNUM.equals(key)) {
				continue;
			}
			key = key.substring(2);
			pc.excelHeader = cols.get(key);
			boolean found = false;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				String headerValue = row.getCell(i).getStringCellValue();
				if (cols.get(key).equals(headerValue)) {
					pc.excelIndex = i;
					found = true;
					break;
				}
			}
			if (!found) {
				throw new Exception("Error: Not found excel header:key=" + key + ",header=" + cols.get(key));
			}
		}

	}

	private void executeImportVerify(ImportRequest req, ImportResponse rsp) throws Exception {
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getStatement(req.verifyMethod);
			stmt.setObject(1, req.token);// fixed parameter
			stmt.setObject(2, req.userCode);// fixed parameter
			stmt.executeQuery();

			rs = stmt.getResultSet();
			List<VO> list = U.getDataFromResultSet(rs);

			FileInputStream fis = new FileInputStream(req.fullPath);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet1 = wb.getSheetAt(0);
			XSSFRow row0 = sheet1.getRow(0);
			int n = sheet1.getLastRowNum();
			rsp.totalNum = n;
			rsp.successNum = n;

			CellStyle style = wb.createCellStyle();
			Font font1 = wb.createFont();
			font1.setFontName("Arial");
			font1.setFontHeightInPoints((short) 11);
			style.setFont(font1);
			style.setFillForegroundColor(IndexedColors.RED1.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			if (rs != null && list.size() > 0) {
				int ncol = row0.getLastCellNum();
				for (VO vo : list) {
					int rownum = Integer.valueOf(vo.get("rownum").toString());
					Row row = sheet1.getRow(rownum);
					Cell cell = row.createCell(ncol);
					cell.setCellValue(vo.get("error_message").toString());
					for (int i = 0; i < ncol + 1; i++) {// need set every cell instead of setRowStyle
						row.getCell(i).setCellStyle(style);
					}
				}
				rsp.errorNum = list.size();
				rsp.successNum = rsp.totalNum - rsp.errorNum;
			}
			rsp.message = "Total=" + rsp.totalNum + ", success=" + rsp.successNum + ", error=" + rsp.errorNum;
			File file = new File(req.fullPath);
			try (OutputStream fileOut = new FileOutputStream(file)) {
				wb.write(fileOut);
				;
			}
			fis.close();
			wb.close();
			rsp.result = C.RESULT_SUCCESS;
		} catch (Exception e) {
			rsp.result = C.RESULT_FAIL;
			rsp.message = e.getMessage();
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			close(rs);
			close(stmt);
		}
	}
}