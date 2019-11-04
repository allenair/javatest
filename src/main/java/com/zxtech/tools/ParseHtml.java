package com.zxtech.tools;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ParseHtml {

	public static void main(String[] args) throws Exception{
		StringBuilder sb = new StringBuilder();
		try (BufferedReader fin = new BufferedReader(new FileReader("f:/test.html"))) {
			fin.lines().forEach(line -> {
				sb.append(line);
			});
		}
		
		parseHtml(sb.toString());
	}

	private static void parseHtml(String htmlDoc) {
		Document doc = Jsoup.parse(htmlDoc);
		
		Elements elementList = doc.getElementsByTag("input");
		
		elementList.forEach(e->{
			if(e.hasAttr("value")) {
				System.out.println(e.attr("value"));
			}
		});
		
		System.out.println(elementList);
	}
}
