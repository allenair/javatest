package com.sinyd.office;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class DealExcelLink {

	public static void main(String[] args)throws Exception {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("e:\\web.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		
		// 创建单元格样式
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

	    // 设置边框
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    
		for(int i=0; i<wb.getNumberOfSheets(); i++){
			HSSFSheet sheet = wb.getSheetAt(i);
			Iterator<Row> rowIter = sheet.rowIterator();
			while (rowIter.hasNext()) {
				Row r = rowIter.next();
				Cell c = r.getCell(4);
				String[] url = getUrl(c.getStringCellValue());

				if (url[1].length() > 0) {
					HSSFHyperlink link = new HSSFHyperlink(
							HSSFHyperlink.LINK_URL);
					c.setCellValue(url[0]);
					link.setAddress(url[1]);
					c.setHyperlink(link);// 设定单元格的链接
					Cell newCell = r.createCell(5);
					newCell.setCellStyle(style);
					newCell.setCellValue(url[1]);
				}
			}
		}
		FileOutputStream fout = new FileOutputStream("e:\\aa.xls");
		wb.write(fout);
		fout.close();
	}

	public static String[] getUrl(String urlStr) {
		String[] res = {"",""};
		if (urlStr == null) {
			return res;
		}
		int index = urlStr.indexOf("http://");
		if (index > 0) {
			res[0] = urlStr.substring(0, index).trim();
			res[1] = urlStr.substring(index).trim();
		}else{
			res[0] = urlStr.trim();
		}
		return res;
	}
}
