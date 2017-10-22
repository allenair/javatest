package com.zxtech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Base64;


public class Base64Test {
	public static void main(String[] args)throws Exception {
//		new Base64Test().start("/Users/allen/Desktop/a.JPG", "/Users/allen/Desktop/res.txt");
		new Base64Test().diff("/Users/allen/Desktop/newbase64.txt", "/Users/allen/Desktop/base64.txt", "/Users/allen/Desktop/res.html");

	}

	public void start(String fileName, String resFileName)throws Exception{
		FileInputStream fin = new FileInputStream(fileName);
		byte[] fileArr = new byte[fin.available()];
		int len = fin.read(fileArr);
		System.out.println("SRC LEN "+len);
		fin.close();
		
//		byte[] resArr = Base64.getEncoder().encode(fileArr);
		byte[] resArr = Base64.getMimeEncoder().encode(fileArr);
		String resArr2Str = new String(resArr);
		String resStr = Base64.getEncoder().encodeToString(fileArr);
		
		PrintWriter fout = new PrintWriter(resFileName);
		fout.write(resStr);
		fout.close();
	}
	
	public void diff(String first, String second, String resHtml)throws Exception{
		BufferedReader f1 = new BufferedReader(new FileReader(new File(first)));
		BufferedReader f2 = new BufferedReader(new FileReader(new File(second)));
		StringBuilder fb1 = new StringBuilder();
		StringBuilder fb2 = new StringBuilder();
		
		String fstr1 = f1.readLine();
		String fstr2 = f2.readLine();
		int count=0;
		for (int i=0;i<fstr1.length()&&i<fstr2.length();i++) {
			char c1 = fstr1.charAt(i);
			char c2 = fstr2.charAt(i);
			if(c1==c2){
				fb1.append(c1);
				fb2.append(c2);
			}else{
				fb1.append("<font color='red'>").append(c1).append("</font>");
				fb2.append("<font color='red'>").append(c2).append("</font>");
				count++;
			}
		}
		
		this.md5(fstr1, fstr2);
		
		System.out.println("len1:"+fstr1.length());
		System.out.println("len2:"+fstr2.length());
		System.out.println("count:"+count);
		
		PrintWriter pout = new PrintWriter(resHtml);
		pout.println("<html><head></head><body>");
		pout.println("<div style='width:550px; border:1px solid #F00; height:320px; display:block'><span>");
		pout.println(fb1.toString());
		pout.println("</span></div>");
		pout.println("<p><p>");
		pout.println("<div style='width:550px; border:1px solid #F00; height:320px; display:block'><span>");
		pout.println(fb2.toString());
		pout.println("</span></div>");
		pout.println("</body></html>");
		pout.flush();
		
		f1.close();
		f2.close();
		pout.close();
	}
	
	private void md5(String f1, String f2)throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] a1 = md.digest(f1.getBytes());
		byte[] a2= md.digest(f2.getBytes());
		for (byte b : a1) {
			System.out.print(b);
			System.out.print(' ');
		}
		System.out.println();
		for (byte b : a2) {
			System.out.print(b);
			System.out.print(' ');
		}
		System.out.println();
	}
}
