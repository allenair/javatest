package allen.test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class PoiTestForLab {
	private Workbook wb;
	private Sheet sheet;
	private CellStyle normalCs;
	private int rowNum=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Map<String, Object>> reportMetaList = new ArrayList<Map<String,Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("report_name", "实验室数据统计报告(综合类)");
		row.put("proj_simple_name", "沈阳绕城高速改扩建工程");
		row.put("report_date", "2013-03-21");
		row.put("start_date", "2012-07-01");
		row.put("end_date", "2013-03-21");
		reportMetaList.add(row);
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		row = new HashMap<String, Object>();
		row.put("field_name","合同段一标");
		row.put("lab_num","12");
		row.put("period_test_num","22");
		row.put("total_test_num","55");
		row.put("period_auto_test_num","23");
		row.put("total_auto_test_num","66");
		row.put("preiod_cm_115","33");
		row.put("total_cm_115","66");
		row.put("period_cm_150","44");
		row.put("total_cm_150","77");
		row.put("period_steel_rate","44");
		row.put("total_steel_rate","876");
		row.put("period_delete_rate","22");
		row.put("total_delete_rate","45");
		row.put("period_event_num","34");
		row.put("total_event_num","76");
		row.put("period_normalized_rate","12");
		row.put("total_normalized_rate","43");
		row.put("period_online_test_rate","12");
		row.put("total_online_test_rate","43");
		row.put("period_global_rate","21");
		row.put("total_global_rate","43");
		row.put("period_application_day","4");
		row.put("total_application_day","6");
		list.add(row);
		
		row = new HashMap<String, Object>();
		row.put("field_name","合同段二标");
		row.put("lab_num","12");
		row.put("period_test_num","22");
		row.put("total_test_num","55");
		row.put("period_auto_test_num","23");
		row.put("total_auto_test_num","66");
		row.put("preiod_cm_115","33");
		row.put("total_cm_115","66");
		row.put("period_cm_150","44");
		row.put("total_cm_150","77");
		row.put("period_steel_rate","44");
		row.put("total_steel_rate","876");
		row.put("period_delete_rate","22");
		row.put("total_delete_rate","45");
		row.put("period_event_num","34");
		row.put("total_event_num","76");
		row.put("period_normalized_rate","12");
		row.put("total_normalized_rate","43");
		row.put("period_online_test_rate","12");
		row.put("total_online_test_rate","43");
		row.put("period_global_rate","21");
		row.put("total_global_rate","43");
		row.put("period_application_day","4");
		row.put("total_application_day","6");
		list.add(row);
		
		
		
		
		
		PoiTestForLab test = new PoiTestForLab();
		test.initExcel();
		test.printExcelHead(reportMetaList.get(0));
		test.printExcelData(list);
		
		test.generateExcel();
	}

	public void initExcel(){
		wb = new HSSFWorkbook();    //建立新HSSFWorkbook对象
		sheet = wb.createSheet("实验室");  //建立新的sheet对象
		sheet.setColumnWidth(0, 5000);
		sheet.setDefaultColumnWidth(10);
		
		
		Font normalFont = wb.createFont();
		normalFont.setFontHeightInPoints((short)10);
		
		normalCs = wb.createCellStyle();
		normalCs.setAlignment(CellStyle.ALIGN_CENTER);
		normalCs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		normalCs.setBorderBottom(CellStyle.BORDER_THIN);
		normalCs.setBorderLeft(CellStyle.BORDER_THIN);
		normalCs.setBorderRight(CellStyle.BORDER_THIN);
		normalCs.setBorderTop(CellStyle.BORDER_THIN);
		normalCs.setFont(normalFont);
		normalCs.setWrapText(true);
	}
	
	public void generateExcel(){
		try{
			FileOutputStream fos = new FileOutputStream("E:/wlab.xls");
			wb.write(fos);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void printExcelData(List<Map<String, Object>> rows){
		Row row;
		Cell c;
		try{
			for (Map<String, Object> rowData : rows) {
				int index=0;
				row = sheet.createRow(rowNum);
				rowNum++;
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("field_name").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("lab_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_test_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_test_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_auto_test_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_auto_test_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("preiod_cm_115").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_cm_115").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_cm_150").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_cm_150").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_steel_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_steel_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_delete_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_delete_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_event_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_event_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_normalized_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_normalized_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_online_test_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_online_test_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_global_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_global_rate").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("period_application_day").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("total_application_day").toString());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void printExcelHead(Map<String, Object> metaData){
		try {
			Font titleFont = wb.createFont();
			titleFont.setFontHeightInPoints((short)20);
			titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			
			CellStyle titleCs = wb.createCellStyle();
			titleCs.setAlignment(CellStyle.ALIGN_CENTER);
			titleCs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleCs.setBorderBottom(CellStyle.BORDER_THIN);
			titleCs.setBorderLeft(CellStyle.BORDER_THIN);
			titleCs.setBorderRight(CellStyle.BORDER_THIN);
			titleCs.setBorderTop(CellStyle.BORDER_THIN);
			titleCs.setFont(titleFont);
				
			// ===============================================================
			
			Font normalFont = wb.createFont();
			normalFont.setFontHeightInPoints((short)10);
			
			CellStyle subTitleCs = wb.createCellStyle();
			subTitleCs.setAlignment(CellStyle.ALIGN_LEFT);
			subTitleCs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			subTitleCs.setBorderBottom(CellStyle.BORDER_THIN);
			subTitleCs.setBorderLeft(CellStyle.BORDER_THIN);
			subTitleCs.setBorderRight(CellStyle.BORDER_THIN);
			subTitleCs.setBorderTop(CellStyle.BORDER_THIN);
			subTitleCs.setFont(normalFont);
			subTitleCs.setWrapText(true);
						
			// ===============================================================
			
			Font columnTitleFont = wb.createFont();
			columnTitleFont.setFontHeightInPoints((short)10);
			columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			
			CellStyle columnTitleCs = wb.createCellStyle();
			columnTitleCs.setAlignment(CellStyle.ALIGN_CENTER);
			columnTitleCs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			columnTitleCs.setBorderBottom(CellStyle.BORDER_THIN);
			columnTitleCs.setBorderLeft(CellStyle.BORDER_THIN);
			columnTitleCs.setBorderRight(CellStyle.BORDER_THIN);
			columnTitleCs.setBorderTop(CellStyle.BORDER_THIN);
			columnTitleCs.setFont(columnTitleFont);
			columnTitleCs.setWrapText(true);
			
			
			
			// =============标题=====================
			Row row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(30);
			Cell c = row.createCell(0);
			c.setCellStyle(titleCs);
			c.setCellValue(metaData.get("report_name").toString());
			
			// 合并单元格--标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 23));   
			
			// =============表头=======================
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("报告日期：  "+metaData.get("report_date").toString());
			for (int i = 1; i <= 23; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("报告区间：  "+metaData.get("start_date").toString()+" ~ "+metaData.get("end_date").toString());
			for (int i = 1; i <= 23; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("工程项目：  "+metaData.get("proj_simple_name").toString());
			for (int i = 1; i <= 23; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			

			// 合并单元格--表头
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 23));  
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 23));  
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 23)); 
						
						
			// =============列头111=======================
			row = sheet.createRow(rowNum);
			row.setHeightInPoints(28);
			
			c = row.createCell(0);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("项目/单位名称");
			
			
			c = row.createCell(1);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("试验室数量");


			
			c = row.createCell(2);
			row.createCell(3).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("试验数量");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 3));  
			
			c = row.createCell(4);
			row.createCell(5).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("自动采集试验数量");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));  
			
			c = row.createCell(6);
			row.createCell(7).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("混凝土抗压强度低于设计值115%"); 
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 7));  
			
			c = row.createCell(8);
			row.createCell(9).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("混凝土抗压强度高于设计值150%"); 
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 8, 9));  
			
			c = row.createCell(10);
			row.createCell(11).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("钢筋强度不合格");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 10, 11));  
			
			c = row.createCell(12);
			row.createCell(13).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("删除不合格试验");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 12, 13));  

			c = row.createCell(14);
			row.createCell(15).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("自动采集试件数量");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 14, 15));  

			c = row.createCell(16);
			row.createCell(17).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("作业不规范试件");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 16, 17));  
			
			c = row.createCell(18);
			row.createCell(19).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("在线试验率");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 18, 19));  
			
			c = row.createCell(20);
			row.createCell(21).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("预警率");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 20, 21));  
			
			c = row.createCell(22);
			row.createCell(23).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("应用天数");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 22, 23)); 
			
			rowNum++;
			
			// =============列头222=======================
			row = sheet.createRow(rowNum);
			
			row.setHeightInPoints(20);
			
			c = row.createCell(0);
			c.setCellStyle(columnTitleCs);
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum, 0, 0));  
			
			c = row.createCell(1);
			c.setCellStyle(columnTitleCs);
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum, 1, 1)); 
			
			c = row.createCell(2);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(个)");
			
			c = row.createCell(3);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(个)");
			
			c = row.createCell(4);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(个)");
			
			c = row.createCell(5);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(个)");
			
			c = row.createCell(6);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(7);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(8);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(9);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(10);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(11);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(12);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(13);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(14);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(个)");
			
			c = row.createCell(15);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(个)");
			
			c = row.createCell(16);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(17);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(18);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(19);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(20);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(%)");
			
			c = row.createCell(21);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(%)");
			
			c = row.createCell(22);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(天)");
			
			c = row.createCell(23);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(天)");
			
			rowNum++;
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
