package allen.encode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringDecode {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		StringDecode test = new StringDecode();
		
		BufferedReader fin = new BufferedReader(new FileReader("f:/aa.txt"));
		PrintWriter pout = new PrintWriter("f:/aa1.txt");
		String tmp;
		while((tmp=fin.readLine())!=null){
			tmp = test.returnDecodeStr(tmp);
			pout.println(tmp);
			System.out.println(tmp);
		}
		
		pout.close();
		fin.close();
		
	}

	public String returnDecodeStr(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}
	
}
