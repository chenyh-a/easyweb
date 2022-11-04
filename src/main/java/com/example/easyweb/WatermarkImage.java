package com.example.easyweb;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
public class WatermarkImage {

	public static void write(XSSFWorkbook wb, Watermark w) throws Exception {

		BufferedImage image = createImage(w);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);

		int idx = wb.addPicture(os.toByteArray(), Workbook.PICTURE_TYPE_PNG);
		POIXMLDocumentPart pd = wb.getAllPictures().get(idx);
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			XSSFSheet sheet = wb.getSheetAt(i);
			PackagePartName ppn = pd.getPackagePart().getPartName();
			String relType = XSSFRelation.IMAGES.getRelation();
			PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
			sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
		}

	}

	private static BufferedImage createImage(Watermark w) {

		BufferedImage image = new BufferedImage(w.width, w.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		image = g.getDeviceConfiguration().createCompatibleImage(w.width, w.height, Transparency.TRANSLUCENT);
		g.dispose();

		g = image.createGraphics();
		// set color
		g.setColor(new Color(Integer.parseInt(w.color.substring(1), 16)));
		g.setFont(w.font);
		g.shear(0.1, -0.26);

		// set smooth effects
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// start Y point
		int y = w.font.getSize() * 3 - 10;
		// draw text
		g.drawString(w.text, 0, y);
		y += w.font.getSize();
		if (w.writeDate) {
			// date using smaller size
			Font f2 = new Font(w.font.getName(), w.font.getStyle(), w.font.getSize() - 5);
			g.setFont(f2);
			String d = new SimpleDateFormat(w.dateFormat).format(new Date());
			// draw date
			g.drawString(d, 0, y);
		}
		g.dispose();
		return image;

	}

	public static class Watermark {

		public boolean writeDate;
		public String text;
		public String dateFormat;
		public String color;
		public Font font;
		public int width;
		public int height;
	}

}