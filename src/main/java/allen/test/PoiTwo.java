package allen.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class PoiTwo {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		try (FileInputStream fileIn = new FileInputStream("d:/ggh.xls")) {
            POIFSFileSystem fs = new POIFSFileSystem(fileIn);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            
            int count = sheet.getLastRowNum();
            System.out.println(count);
            try(PrintWriter wrt = new PrintWriter("d:/out.txt","utf-8")){
	            for(int i=0;i<count;i++) {
	            	try {
	            		HSSFRow row = sheet.getRow(i);
		            	HSSFCell cell = row.getCell(0);
		            	String tmp = cell.getStringCellValue();
		            	wrt.write(change(tmp)+"\n");
	            	}catch(Exception e) {
	            		System.out.println(i+"===");
	            		continue;
	            	}
	            	
	            	

	            }
	            wrt.flush();
            }
            
            
        }

	}
	
	private static String change(String tmp) {
		tmp = tmp.trim();
		if(StringUtils.isBlank(tmp)) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<tmp.length();i++) {
			char c = tmp.charAt(i);
			if(c=='\n' || c=='\r') {
				continue;
			}
			sb.append(c);
		}
		
		return sb.toString();
	}

}
