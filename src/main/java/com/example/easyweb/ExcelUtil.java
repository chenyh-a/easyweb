package com.example.easyweb;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.easyweb.WatermarkImage.Watermark;
import com.example.easyweb.vo.ExportRequest;
import com.example.easyweb.vo.ExportResponse;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	public static void export(ResultSet rs, ExportRequest req, ExportResponse rsp) throws Exception {

		int n = 0;
		File file = getDestFile(req, rsp);
		if (rs != null) {

			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet1 = wb.createSheet("My Sheet 1");
			XSSFRow row0 = sheet1.createRow(n);
			
			// create header style
			XSSFCellStyle style0 = createStyle(wb, "Arial", 11, IndexedColors.GREY_25_PERCENT.getIndex());
			// create content style
			XSSFCellStyle style1 = createStyle(wb, "Arial", 10, (short) -1);

			int i = 0;
			for (String key : req.cols.keySet()) {
				XSSFCell cell = row0.createCell(i);
				cell.setCellValue(req.cols.get(key).toString());
				cell.setCellStyle(style0);
				i++;
			}

			while (rs.next()) {
				XSSFRow row = sheet1.createRow(sheet1.getLastRowNum() + 1);
				i = 0;
				for (String key : req.cols.keySet()) {
					Object val = rs.getObject(key);
					XSSFCell cell = row.createCell(i);
					if (val instanceof Integer) {
						cell.setCellValue(Integer.valueOf(val.toString()));
					} else if (val instanceof String) {
						cell.setCellValue(String.valueOf(val.toString()));
					}
					cell.setCellStyle(style1);
					i++;
				}
				// write row to excel file.
				n++;
			}
			if (req.autoSizeColumn) {
				for (int j = 0; j < req.cols.size(); j++) {
					sheet1.autoSizeColumn(j);
				}
			}
			if (req.watermark) {
				drawWatermark(wb, req.userCode);
			}
			try (OutputStream fileOut = new FileOutputStream(file)) {
				wb.write(fileOut);
			}
			wb.close();

			log.debug("Exported file path:" + file.getAbsolutePath());
		}
		rsp.totalNum = n;
		rsp.successNum = n;
		rsp.result = Constants.RESULT_SUCCESS;
	}

	private static File getDestFile(ExportRequest req, ExportResponse rsp) {
		Date date = new Date();
		String today = new SimpleDateFormat("yyyyMMdd").format(date);
		String sdir = "export/" + today;
		File fulldir = new File(req.currRootDir + sdir);
		if (!fulldir.exists()) {
			fulldir.mkdirs();
		}

		String fname = req.filename;
		if (fname == null || "".equals(fname)) {
			fname = "export";
		} else {
			fname = fname.trim().replaceAll(" ", "");
			fname = fname.replaceAll("/", "");
		}
		fname += "-" + req.userCode + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss-S").format(date) + ".xlsx";
		rsp.fileUrl = sdir + "/" + fname;
		File file = new File(fulldir, fname);
		return file;
	}

	private static XSSFCellStyle createStyle(XSSFWorkbook wb, String fontName, int fontSize, short backColor) {
		XSSFFont font = wb.createFont();
		font.setFontName(fontName);
		font.setFontHeightInPoints((short) fontSize);
		XSSFCellStyle style0 = wb.createCellStyle();
		if (backColor > 0) {
			style0.setFillForegroundColor(backColor);
			style0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		style0.setFont(font);
		return style0;
	}

	private static void drawWatermark(XSSFWorkbook wb, String watermarkText) throws Exception {
		Watermark w = new Watermark();
		/* use user code as watermark text */
		w.text = watermarkText;
		w.writeDate = true;
		w.dateFormat = "yyyy-MM-dd HH:mm";
		w.color = "#d3d7d4";
		w.font = new Font("Arial", Font.PLAIN | Font.BOLD, 30);

		w.width = 400;
		w.height = 200;
		WatermarkImage.write(wb, w);
	}
}
