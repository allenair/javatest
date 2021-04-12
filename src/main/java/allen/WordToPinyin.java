package allen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class WordToPinyin {
	/**
	 * 汉字转换位汉语拼音，英文字符不变
	 * 
	 * @author xuke
	 *
	 */

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	private  String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	/**
	 * 汉字转换位汉语拼音，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	private  String converterToSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	// 区域,二级网路,销售员,驻地,辖区,职位
	public List<Map<String, String>> getRecordFromFile(String fileName) throws Exception{
		List<Map<String, String>> result = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);
			HashMap<String, String> row = new HashMap<>();
			row.put("area", getValue(arr[0]));
			row.put("secondnt", getValue(arr[1]));
			row.put("saler", getValue(arr[2]));
			row.put("place", getValue(arr[3]));
			row.put("dealarea", getValue(arr[4]));
			row.put("pos", getValue(arr[5]));
			
			row.put("salerpin", getPinyinName(getValue(arr[2])));
			result.add(row);
		}
		br.close();
		return result;
	}
	
	//Id,LoginName,LoginPassword,UserName,E_mail,Mobile,Department,Duty,Job,SuperiorId,Area,Company,City,Encampment
	public void writeToFile(String fileName, List<Map<String, String>> rows)throws Exception{
		PrintWriter pw = new PrintWriter(fileName, "gbk");
		pw.println("Id,LoginName,LoginPassword,UserName,E_mail,Mobile,Department,Duty,Job,SuperiorId,Area,Company,City,Encampment");
		
		
		int index=0;
		for (Map<String, String> row : rows) {
			index++;
			StringBuilder sb = new StringBuilder();
			sb.append(""+index).append(",");
			sb.append(row.get("salerpin")).append(",");
			sb.append(",");
			sb.append(row.get("saler")).append(",");
			sb.append(",");
			sb.append(",");
			sb.append("销售部,");
			sb.append(row.get("pos")).append(",");
			sb.append(",");
			sb.append(",");
			sb.append(row.get("area")).append(",");
			sb.append(row.get("secondnt")).append(",");
			sb.append(row.get("dealarea")).append(",");
			sb.append(row.get("place"));
			
			pw.println(sb.toString());
		}
		
		pw.flush();
		pw.close();
	}
	
	private String getPinyinName(String name){
		if(name.length()==0)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(converterToFirstSpell(name.substring(0,name.length()-1)));
		sb.append(converterToSpell(name.substring(name.length()-1)));
		return sb.toString();
	}
	
	private String getValue(String tmp){
		if(tmp==null){
			return "";
		}else{
			return tmp.trim();
		}
	}
	public static void main(String[] args) throws Exception{
		WordToPinyin test = new WordToPinyin();
		System.out.println(test.converterToSpell("欢迎来到最棒的Java中文社区"));

//		List<Map<String, String>> rows = test.getRecordFromFile("/Users/allen/Desktop/Book1.csv");
//		test.writeToFile("/Users/allen/Desktop/result-gbk.csv", rows);
//		System.out.println(1);
	}

}
