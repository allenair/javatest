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
import org.apache.poi.ss.util.Region;


public class PoiTest {

	private Workbook wb;
	private Sheet sheet;
	private CellStyle normalCs;
	private int rowNum=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int maxOil, maxCm, maxMm;
		List<Map<String, Object>> numList = new ArrayList<Map<String,Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("max_oil", 3);
		row.put("max_cm", 2);
		row.put("max_mm", 2);
		numList.add(row);
		
		maxOil = new Integer(numList.get(0).get("max_oil").toString());
		maxCm = new Integer(numList.get(0).get("max_cm").toString());
		maxMm = new Integer(numList.get(0).get("max_mm").toString());
		
		
		List<Map<String, Object>> reportMetaList = new ArrayList<Map<String,Object>>();
		row = new HashMap<String, Object>();
		row.put("report_name", "拌和站数据统计报告(综合类)");
		row.put("proj_simple_name", "沈阳绕城高速改扩建工程");
		row.put("report_date", "2013-03-21");
		row.put("start_date", "2012-07-01");
		row.put("end_date", "2013-03-21");
		reportMetaList.add(row);
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		row = new HashMap<String, Object>();
		row.put("field_name","合同段一标");
		row.put("truck_period_num","0");
		row.put("truck_total_num","0");
		row.put("am_period_ton","468.5");
		row.put("am_total_ton","468.5");
		row.put("cm_period_ton","123.23");
		row.put("cm_total_ton","123.23");
		row.put("mm_period_ton","456.65");
		row.put("mm_total_ton","456.65");
		row.put("am_period_num","166");
		row.put("am_period_alert_num","27");
		row.put("am_period_alert","16.27");
		row.put("am_total_num","166");
		row.put("am_total_alert_num","27");
		row.put("am_total_alert","16.27");
		row.put("mm_period_num","1");
		row.put("mm_period_alert_num","2");
		row.put("mm_period_alert","3");
		row.put("mm_total_num","4");
		row.put("mm_total_alert_num","5");
		row.put("mm_total_alert","6");
		row.put("cm_period_num","7");
		row.put("cm_period_alert_num","8");
		row.put("cm_period_alert","9");
		row.put("cm_total_num","10");
		row.put("cm_total_alert_num","11");
		row.put("cm_total_alert","12");
		
		row.put("avg_oil_stone_ratio","ac20<11<12>ac25<13<14");
		row.put("oil_stone_ratio_num","2");
		
		row.put("modifier_ratio","aacc11<20<21");
		row.put("modifier_num","1");
		
		row.put("cm_ratio","cm20<30<31>cm25<32<33");
		row.put("cm_num","2");
		
		int count=0;
		String[] groupArr = row.get("avg_oil_stone_ratio").toString().split(">");
		if(row.get("avg_oil_stone_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("avg_oil_stone_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxOil; i++){
			row.put("avg_oil_stone_ratio_"+i+"_0","");
			row.put("avg_oil_stone_ratio_"+i+"_1","");
			row.put("avg_oil_stone_ratio_"+i+"_2","");
		}
		
		groupArr = row.get("modifier_ratio").toString().split(">");
		if(row.get("modifier_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("modifier_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxMm; i++){
			row.put("modifier_ratio_"+i+"_0","");
			row.put("modifier_ratio_"+i+"_1","");
			row.put("modifier_ratio_"+i+"_2","");
		}
		
		groupArr = row.get("cm_ratio").toString().split(">");
		if(row.get("cm_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("cm_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxCm; i++){
			row.put("cm_ratio_"+i+"_0","");
			row.put("cm_ratio_"+i+"_1","");
			row.put("cm_ratio_"+i+"_2","");
		}
		
		list.add(row);
		
		
		
		
		row = new HashMap<String, Object>();
		row.put("field_name","合同段22标");
		row.put("truck_period_num","0");
		row.put("truck_total_num","0");
		row.put("am_period_ton","1468.5");
		row.put("am_total_ton","1468.5");
		row.put("cm_period_ton","2123.23");
		row.put("cm_total_ton","1123.23");
		row.put("mm_period_ton","1456.65");
		row.put("mm_total_ton","1456.65");
		row.put("am_period_num","1166");
		row.put("am_period_alert_num","57");
		row.put("am_period_alert","16.57");
		row.put("am_total_num","366");
		row.put("am_total_alert_num","57");
		row.put("am_total_alert","36.27");
		row.put("mm_period_num","1");
		row.put("mm_period_alert_num","2");
		row.put("mm_period_alert","3");
		row.put("mm_total_num","4");
		row.put("mm_total_alert_num","5");
		row.put("mm_total_alert","6");
		row.put("cm_period_num","7");
		row.put("cm_period_alert_num","8");
		row.put("cm_period_alert","9");
		row.put("cm_total_num","10");
		row.put("cm_total_alert_num","11");
		row.put("cm_total_alert","12");
		
		row.put("avg_oil_stone_ratio","ac20<11<12>ac25<13<14>ac29<15<16");
		row.put("oil_stone_ratio_num","3");
		
		row.put("modifier_ratio","aacc11<20<21>aacc25<23<24");
		row.put("modifier_num","2");
		
		row.put("cm_ratio","");
		row.put("cm_num","0");
		
		
		groupArr = row.get("avg_oil_stone_ratio").toString().split(">");
		if(row.get("avg_oil_stone_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("avg_oil_stone_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxOil; i++){
			row.put("avg_oil_stone_ratio_"+i+"_0","");
			row.put("avg_oil_stone_ratio_"+i+"_1","");
			row.put("avg_oil_stone_ratio_"+i+"_2","");
		}
		
		groupArr = row.get("modifier_ratio").toString().split(">");
		if(row.get("modifier_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("modifier_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxMm; i++){
			row.put("modifier_ratio_"+i+"_0","");
			row.put("modifier_ratio_"+i+"_1","");
			row.put("modifier_ratio_"+i+"_2","");
		}
		
		groupArr = row.get("cm_ratio").toString().split(">");
		if(row.get("cm_ratio").toString().length()==0){
			count=0;
		}else{
			count = groupArr.length;
		}
		for(int i=0; i<count; i++){
			String[] innerArr = groupArr[i].split("<");
			if(innerArr.length!=3){
				continue;
			}
			for(int k=0;k<innerArr.length;k++){
				row.put("cm_ratio_"+i+"_"+k,innerArr[k]);
			}
		}
		for(int i=count; i<maxCm; i++){
			row.put("cm_ratio_"+i+"_0","");
			row.put("cm_ratio_"+i+"_1","");
			row.put("cm_ratio_"+i+"_2","");
		}
		
		list.add(row);
		
		
		
		PoiTest test = new PoiTest();
		test.initExcel();
		test.printExcelHead(maxOil, maxMm, maxCm, reportMetaList.get(0));
		test.printExcelData(maxOil, maxMm, maxCm, list);
		
		test.generateExcel();
		
		
	}
	
	public void initExcel(){
		wb = new HSSFWorkbook();    //建立新HSSFWorkbook对象
		sheet = wb.createSheet("拌合站");  //建立新的sheet对象
		sheet.setColumnWidth(0, 5000);
//		sheet.setColumnWidth(1, 1600);
//		sheet.setColumnWidth(2, 1600);
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
			FileOutputStream fos = new FileOutputStream("E:/w1.xls");
			wb.write(fos);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void printExcelData(int maxOil, int maxMm, int maxCm, List<Map<String, Object>> rows){
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
				c.setCellValue(rowData.get("truck_period_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("truck_total_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_period_ton").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_total_ton").toString());

				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_period_ton").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_total_ton").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_period_ton").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_total_ton").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_period_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_period_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_period_alert").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_total_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_total_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("am_total_alert").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_period_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_period_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_period_alert").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_total_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_total_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("mm_total_alert").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_period_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_period_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_period_alert").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_total_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_total_alert_num").toString());
				
				c = row.createCell(index++);
				c.setCellStyle(normalCs);
				c.setCellValue(rowData.get("cm_total_alert").toString());
				
				for(int i=0;i<maxOil;i++){
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("avg_oil_stone_ratio_"+i+"_0").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("avg_oil_stone_ratio_"+i+"_1").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("avg_oil_stone_ratio_"+i+"_2").toString());
				}
				
				for(int i=0;i<maxMm;i++){
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("modifier_ratio_"+i+"_0").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("modifier_ratio_"+i+"_1").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("modifier_ratio_"+i+"_2").toString());
				}
				
				for(int i=0;i<maxCm;i++){
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("cm_ratio_"+i+"_0").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("cm_ratio_"+i+"_1").toString());
					
					c = row.createCell(index++);
					c.setCellStyle(normalCs);
					c.setCellValue(rowData.get("cm_ratio_"+i+"_2").toString());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void printExcelHead(int maxOil, int maxMm, int maxCm, Map<String, Object> metaData){
		int oilColumnNumber = 3*maxOil;
		int modifierColumnNumber = 3*maxMm;
		int cmColumnNumber = 3*maxCm;
		int allColumnNumber = 27 + oilColumnNumber + modifierColumnNumber + cmColumnNumber;
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
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, allColumnNumber-1));   
			
			// =============表头=======================
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("报告日期：  "+metaData.get("report_date").toString());
			for (int i = 1; i <= allColumnNumber-1; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("报告区间：  "+metaData.get("start_date").toString()+" ~ "+metaData.get("end_date").toString());
			for (int i = 1; i <= allColumnNumber-1; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			row = sheet.createRow(rowNum);
			rowNum++;
			row.setHeightInPoints(20);
			c = row.createCell(0);
			c.setCellStyle(subTitleCs);
			c.setCellValue("工程项目：  "+metaData.get("proj_simple_name").toString());
			for (int i = 1; i <= allColumnNumber-1; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			
			

			// 合并单元格--表头
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, allColumnNumber-1));  
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, allColumnNumber-1));  
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, allColumnNumber-1)); 
						
						
			// =============列头111=======================
			row = sheet.createRow(rowNum);
			
			row.setHeightInPoints(20);
			
			c = row.createCell(0);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("项目/标段名称");
			
			
			c = row.createCell(1);
			row.createCell(2).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("出入场统计");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));  
			
			c = row.createCell(3);
			row.createCell(4).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("沥青混合料生产量");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 4));  
			
			c = row.createCell(5);
			row.createCell(6).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("水泥混凝土生产量");
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 6));  
			
			c = row.createCell(7);
			row.createCell(8).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("改性沥青生产量"); 
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 7, 8));  
			
			c = row.createCell(9);
			row.createCell(10).setCellStyle(normalCs);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("沥青混合料预警率"); 
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 9, 14));  
			
			c = row.createCell(15);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("改性沥青预警率");
			for (int i = 16; i <= 20; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 15, 20));  
			
			c = row.createCell(21);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("水泥混凝土预警率");
			for (int i = 22; i <= 26; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 21, 26));  
			
			c = row.createCell(27);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("平均油石比");
			for (int i = 28; i <= 26+oilColumnNumber; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 27, 26+oilColumnNumber));  
			
			c = row.createCell(27+oilColumnNumber);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("平均改性剂含量");
			for (int i = 28+oilColumnNumber; i <= 26+oilColumnNumber+modifierColumnNumber; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 27+oilColumnNumber, 26+oilColumnNumber+modifierColumnNumber));  
			
			c = row.createCell(27+oilColumnNumber+modifierColumnNumber);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("平均水泥含量");
			for (int i = 28+oilColumnNumber+modifierColumnNumber; i <= 26+oilColumnNumber+modifierColumnNumber+cmColumnNumber; i++) {
				row.createCell(i).setCellStyle(normalCs);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 27+oilColumnNumber+modifierColumnNumber, 26+oilColumnNumber+modifierColumnNumber+cmColumnNumber));  
			
			rowNum++;
			
			
			// =============列头222=======================
			row = sheet.createRow(rowNum);
			
			row.setHeightInPoints(28);
			
			c = row.createCell(0);
			c.setCellStyle(columnTitleCs);
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum, 0, 0));  
			
			c = row.createCell(1);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期数量(条)");
			
			c = row.createCell(2);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计数量(条)");
			
			c = row.createCell(3);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(吨)");
			
			c = row.createCell(4);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(吨)");
			
			c = row.createCell(5);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(吨)");
			
			c = row.createCell(6);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(吨)");
			
			c = row.createCell(7);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期(吨)");
			
			c = row.createCell(8);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计(吨)");
			
			c = row.createCell(9);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期生产(盘)");
			
			c = row.createCell(10);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警(盘)");
			
			c = row.createCell(11);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警率(%)");
			
			c = row.createCell(12);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计生产(盘)");
			
			c = row.createCell(13);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警(盘)");
			
			c = row.createCell(14);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警率(%)");
			
			
			c = row.createCell(15);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期生产(盘)");
			
			c = row.createCell(16);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警(盘)");
			
			c = row.createCell(17);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警率(%)");
			
			c = row.createCell(18);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计生产(盘)");
			
			c = row.createCell(19);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警(盘)");
			
			c = row.createCell(20);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警率(%)");
			
			c = row.createCell(21);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期生产(盘)");
			
			c = row.createCell(22);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警(盘)");
			
			c = row.createCell(23);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("本期预警率(%)");
			
			c = row.createCell(24);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计生产(盘)");
			
			c = row.createCell(25);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警(盘)");
			
			c = row.createCell(26);
			c.setCellStyle(columnTitleCs);
			c.setCellValue("累计预警率(%)");
			
			
			for(int i=0, start=27; i<maxOil+maxMm+maxCm; i++, start+=2){
				c = row.createCell(start+i);
				c.setCellStyle(columnTitleCs);
				c.setCellValue("产品规格");
				
				c = row.createCell(start+i+1);
				c.setCellStyle(columnTitleCs);
				c.setCellValue("本期(%)");
				
				c = row.createCell(start+i+2);
				c.setCellStyle(columnTitleCs);
				c.setCellValue("累计(%)");
			}
			
			rowNum++;
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
