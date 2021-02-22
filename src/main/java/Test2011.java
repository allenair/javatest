import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.docx4j.Docx4J;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.STBrType;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.xwpf.NiceXWPFDocument;

import cn.hutool.core.util.RandomUtil;

public class Test2011 {

	public static void main(String[] args) throws Exception {
		long ss = System.currentTimeMillis();
//		testPoi();
//		copyAndChange();
//		merge();
//		mergeToOne();
		
		System.out.println(System.currentTimeMillis()-ss);
		
//		test1224();
		
		System.out.println(isStrongPasswd("1234567"));
		System.out.println(isStrongPasswd("12345678"));
		System.out.println(isStrongPasswd("asdfghjfghjk"));
		System.out.println(isStrongPasswd("asd34567"));
		System.out.println(isStrongPasswd("asSdf$d34567"));
		
		System.out.println(RandomUtil.randomString(8));
		System.out.println(RandomUtil.randomString(8));
		System.out.println(RandomUtil.randomString(8));
		System.out.println(RandomUtil.randomStringUpper(8));
		System.out.println(RandomUtil.randomStringUpper(8));
		System.out.println(RandomUtil.randomStringUpper(8));
		
		BigDecimal total = new BigDecimal(12);
		BigDecimal count = new BigDecimal(10);
		System.out.println(total.compareTo(count));
		System.out.println(total.subtract(count));
		
		System.out.println(get32BitMd5EncString("a123456!"));
		
		List<String> drugZeroList = new ArrayList<>();
		drugZeroList.add("asd");
		drugZeroList.add("yui");
		System.out.println(String.join(",", drugZeroList));
		
		
		BigDecimal aa = new BigDecimal(1.01);
		System.out.println(aa.intValue());
	}

	public static String get32BitMd5EncString(String plainText) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString(); // md5 32bit
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static boolean isStrongPasswd(String password) {
		if (StringUtils.isBlank(password) || password.length() < 8) {
			return false;
		}

		int count = 0;
		if (password.matches(".*[a-z]{1,}.*")) {
			count++;
		}
		if (password.matches(".*[A-Z]{1,}.*")) {
			count++;
		}
		if (password.matches(".*\\d{1,}.*")) {
			count++;
		}
		if (password.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
			count++;
		}

		return count > 1;
	}
	
	public static void test1224() {
		String commonContent = "<!doctype html><html lang=en><head><title>HTTP Status 404 – Not Found</title><style type=>body {font-family:Tahoma,Arial,sans-serif;} h1, h2, h3, b {color:white;background-color:#525D76;} h1 {font-size:22px;} h2 {font-size:16px;} h3 {font-size:14px;} p {font-size:12px;} a {color:black;} .line {height:1px;background-color:#525D76;border:none;}</style></head><body><h1>HTTP Status 404 – Not Found</h1></body></html>";
		commonContent = commonContent.replaceAll("<", "&lt;");
		commonContent = commonContent.replaceAll(">", "&gt;");
		System.out.println(commonContent);
	}
	
	public static void testPoi() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("docx_word1", new DocxRenderData(new File("D:\\doc_1.docx")));
		map.put("docx_word2", new DocxRenderData(new File("D:\\doc_2.docx")));
		XWPFTemplate template = XWPFTemplate.compile("D:\\template_1.docx").render(map);

		FileOutputStream out = new FileOutputStream("D:\\qqqq.docx");
		template.write(out);
		out.flush();
		out.close();
		template.close();
	}
	
	public static void mergeToOne()  throws Exception {
		List<InputStream> streams = new ArrayList<>();
		for(int i=0;i<50;i++) {
			streams.add(new FileInputStream("d:/aaa/doc_1.docx"));
		}
		
		WordprocessingMLPackage target = mergeDocx(streams, "d:/res.docx");
		
		
//		Docx4J.toPDF(target, new FileOutputStream("d:/out.pdf"));
	}
	
	
	private static WordprocessingMLPackage mergeDocx(final List<InputStream> streams, String path) throws Docx4JException, IOException {
		WordprocessingMLPackage target = null;
		final File generated = new File(path);

		int chunkId = 0;
		Iterator<InputStream> it = streams.iterator();
		while (it.hasNext()) {
			InputStream is = it.next();
			if (is != null) {
				try {
					if (target == null) {
						// Copy first (master) document
						OutputStream os = new FileOutputStream(generated);
						os.write(IOUtils.toByteArray(is));
						os.close();

						target = WordprocessingMLPackage.load(generated);
					} else {
						MainDocumentPart documentPart = target.getMainDocumentPart();

//                        addPageBreak(documentPart); // 另起一页，换页

						insertDocx(documentPart, IOUtils.toByteArray(is), chunkId++);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					is.close();
				}
			}
		}

		if (target != null) {
			target.save(generated);
			// Docx4J.save(target, generated, Docx4J.FLAG_NONE);
			return target;
		} else {
			return null;
		}
	}
	
	private static void insertDocx(MainDocumentPart main, byte[] bytes, int chunkId) {
		try {
			AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(
					new PartName("/part" + chunkId + ".docx"));
			// afiPart.setContentType(new ContentType(CONTENT_TYPE));
			afiPart.setBinaryData(bytes);
			Relationship altChunkRel = main.addTargetPart(afiPart);

			CTAltChunk chunk = Context.getWmlObjectFactory().createCTAltChunk();
			chunk.setId(altChunkRel.getId());

			main.addObject(chunk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addPageBreak(MainDocumentPart documentPart) {
		ObjectFactory factory = new ObjectFactory();
		Br breakObj = new Br();
		breakObj.setType(STBrType.PAGE);

		P paragraph = factory.createP();
		paragraph.getContent().add(breakObj);
		documentPart.getJaxbElement().getBody().getContent().add(paragraph);
	}
	
	public static void merge()  throws Exception {
//		String[] fileArr = new File("d:/aaa").list();
//		List<String> fileList = Stream.of(fileArr).sorted().skip(0).limit(3).collect(Collectors.toList());
//		
		List<NiceXWPFDocument> docList = new ArrayList<>();
//		for(int i=0;i<fileList.size();i++) {
//			docList.add(new NiceXWPFDocument(new FileInputStream("d:/aaa/"+fileList.get(i))));
//		}
		
		for(int i=0;i<30;i++) {
			docList.add(new NiceXWPFDocument(new FileInputStream("d:/aaa/doc_1.docx")));
		}
		
//		NiceXWPFDocument newDoc = docList.get(0);
		NiceXWPFDocument newDoc = new NiceXWPFDocument();
		for(int i=1;i<docList.size();i++) {
			newDoc = newDoc.merge(docList.get(i));
		}
		
		FileOutputStream out = new FileOutputStream("d:/new_doc.docx");
		newDoc.write(out);
		newDoc.close();
		out.close();
		
//		fileList.forEach(System.out::println);
		
		
//		NiceXWPFDocument doc_1 = new NiceXWPFDocument(new FileInputStream("D:\\doc_1.docx"));
//		NiceXWPFDocument doc_2 = new NiceXWPFDocument(new FileInputStream("D:\\doc_2.docx"));
//		NiceXWPFDocument doc_3 = new NiceXWPFDocument(new FileInputStream("D:\\doc_3.docx"));
//		NiceXWPFDocument doc_4 = new NiceXWPFDocument(new FileInputStream("D:\\doc_4.docx"));
//		NiceXWPFDocument doc_5 = new NiceXWPFDocument(new FileInputStream("D:\\doc_5.docx"));
//		
//		List<NiceXWPFDocument> list = new ArrayList<>();
//		list.add(doc_2);
//		list.add(doc_3);
//		list.add(doc_4);
//		list.add(doc_5);
//		
//		
//		NiceXWPFDocument newDoc = doc_1.merge(doc_2);
//		newDoc = newDoc.merge(doc_3);
//		newDoc = newDoc.merge(doc_4);
//
//		// 生成新文档
//		FileOutputStream out = new FileOutputStream("d:/new_doc.docx");
//		newDoc.write(out);
//		newDoc.close();
//		out.close();
	}
	
	public static void copyAndChange()  throws Exception {
		copyFileUsingFileStreams("d:/test01.docx", "d:/test01-copy.docx");
		
		XWPFDocument doc = new XWPFDocument(new FileInputStream("d:/test01-copy.docx"));
		
		XWPFParagraph p1 = doc.createParagraph();
		XWPFRun insertNewRun = p1.insertNewRun(0);
		insertNewRun.setText("在段落起始位置插入这段文本");
		
		// 段落
		List<XWPFParagraph> paragraphs = doc.getParagraphs();
		// 表格
		List<XWPFTable> tables = doc.getTables();
		// 图片
		List<XWPFPictureData> allPictures = doc.getAllPictures();
		// 页眉
		List<XWPFHeader> headerList = doc.getHeaderList();
		// 页脚
		List<XWPFFooter> footerList = doc.getFooterList();
		
		paragraphs.forEach(bean->{
			bean.removeRun(0);
		});
		
		tables.forEach(bean->{
			bean.removeRow(0);
		});
	}
	
	private static void copyFileUsingFileStreams(String source, String dest)
	        throws IOException {    
	    InputStream input = null;    
	    OutputStream output = null;    
	    try {
	           input = new FileInputStream(source);
	           output = new FileOutputStream(dest);        
	           byte[] buf = new byte[1024];        
	           int bytesRead;        
	           while ((bytesRead = input.read(buf)) > 0) {
	               output.write(buf, 0, bytesRead);
	           }
	    } finally {
	        input.close();
	        output.close();
	    }
	}
}
