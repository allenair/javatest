package com.zxtech;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIExcelObjectExtract {

	public static void main(String[] args) throws Exception{
		new POIExcelObjectExtract().extract();

	}

	public void extract() throws Exception{
		XSSFWorkbook workbook = new XSSFWorkbook("/Users/allen/Desktop/Book1.xlsx");
		  for (PackagePart pPart : workbook.getAllEmbedds()) {
		      String contentType = pPart.getContentType();
//		      // Excel Workbook - either binary or OpenXML
//		      if (contentType.equals("application/vnd.ms-excel")) {
//		          HSSFWorkbook embeddedWorkbook = new HSSFWorkbook(pPart.getInputStream());
//		      }
//		      // Excel Workbook - OpenXML file format
//		      else if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//		          OPCPackage docPackage = OPCPackage.open(pPart.getInputStream());
//		          XSSFWorkbook embeddedWorkbook = new XSSFWorkbook(docPackage);
//		      }
//		      // Word Document - binary (OLE2CDF) file format
//		      else if (contentType.equals("application/msword")) {
//		          HWPFDocument document = new HWPFDocument(pPart.getInputStream());
//		      }
//		      // Word Document - OpenXML file format
//		      else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//		          OPCPackage docPackage = OPCPackage.open(pPart.getInputStream());
//		          XWPFDocument document = new XWPFDocument(docPackage);
//		      }
//		      // PowerPoint Document - binary file format
//		      else if (contentType.equals("application/vnd.ms-powerpoint")) {
//		          HSLFSlideShow slideShow = new HSLFSlideShow(pPart.getInputStream());
//		      }
//		      // PowerPoint Document - OpenXML file format
//		      else if (contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
//		          OPCPackage docPackage = OPCPackage.open(pPart.getInputStream());
//		          XSLFSlideShow slideShow = new XSLFSlideShow(docPackage);
//		      }
//		      // Any other type of embedded object.
//		      else {
//		          System.out.println("Unknown Embedded Document: " + contentType);
//		          InputStream inputStream = pPart.getInputStream();
//		      }
		      ContentType a = pPart.getContentTypeDetails();
		      System.out.println(pPart.getPartName().getURI()+"  ##   " + contentType);
		      String[] tmp = pPart.getPartName().getName().split("/");
		      
		      
		      InputStream input = pPart.getInputStream();
//		      InputStream input = new FileInputStream(new File("/Users/allen/Desktop/西奥调研问题.md"));
		      
		      OutputStream output = new FileOutputStream(new File("/Users/allen/Desktop/"+tmp[tmp.length-1]));
		      
		      byte[] buff = new byte[1024];
		      
		      while(input.read(buff)>0){
		    	  output.write(buff);
		      }
		      
		      input.close();
		      output.close();
		  }
	}
}
