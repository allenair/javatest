package com.zxtech;

import java.util.ArrayList;
import java.util.Base64;

public class CollectionDataDeal {

	public static void main(String[] args) {
		String code= "AKwAEgBsAAAA4GsNAQAAAABmkAAAAAAAAA==";
//		System.out.println(getSubString(code, 5, 1));
//		System.out.println(code.substring(3));
		new CollectionDataDeal().getBitValue(code);
	}

	public static String getSubString(String srcStr, int start, int len){
		return srcStr.substring(start, start+len);
	}
	
	public void getByteVal(String baseCode){
		byte[] srcArr = Base64.getDecoder().decode(baseCode);
		for (byte b : srcArr) {
//			System.out.print(b);System.out.print("\t");
//			System.out.print(Integer.toHexString(b&0xff));
			System.out.print(Integer.toHexString(b>>4&0x0f));
			System.out.print(Integer.toHexString(b&0x0f));
			System.out.print(" ");
		}
	}
	
	public void getBitValue(String baseCode){
//		ArrayList<String> resList = new ArrayList();
		StringBuilder strBuffer = new StringBuilder();
		byte[] srcArr = Base64.getDecoder().decode(baseCode);
		for (byte b : srcArr) {
			strBuffer.append(""+(b>>7&0x1));
			strBuffer.append(""+(b>>6&0x1));
			strBuffer.append(""+(b>>5&0x1));
			strBuffer.append(""+(b>>4&0x1));
			strBuffer.append(""+(b>>3&0x1));
			strBuffer.append(""+(b>>2&0x1));
			strBuffer.append(""+(b>>1&0x1));
			strBuffer.append(""+(b>>0&0x1));
			strBuffer.append(" ");
		}
//		for(int k=0; k<resList.size();k++){
//			if(k%8==0)
//				System.out.print(" ");
//			System.out.print(resList.get(k));
//		}
		System.out.println(strBuffer.toString());
//		System.out.println(getValue("01110000101100100000100100000001"));
	}
	
	
	public int getValue(String str){
		return Integer.parseInt(str, 2);
	}
}
