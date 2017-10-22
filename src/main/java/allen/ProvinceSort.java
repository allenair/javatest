package allen;

import java.util.Arrays;
import java.util.HashMap;

//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ProvinceSort {

	private String[] provices = new String[] { "北京市", "天津市", "河北省", "山西省",
			"内蒙古自治区", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省",
			"江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市",
			"四川省", "贵州省", "云南省", "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区",
			"新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾省" };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ProvinceSort().provinceSort();
	}

	private void provinceSort(){
		HashMap<String, String> pinyinMap = new HashMap<String, String>(this.provices.length);
		String pinyin="";
		for (String str : provices) {
			pinyin = this.realPinyin(str, true);
			pinyinMap.put(pinyin, str);
		}
		
		String[] pinyinKeys = pinyinMap.keySet().toArray(new String[0]);
		Arrays.sort(pinyinKeys);
		
		for(int i=0;i<pinyinKeys.length;i++){
			System.out.println(pinyinMap.get(pinyinKeys[i])+"\t\t"+pinyinKeys[i]);
		}
	}
	
	private String realPinyin(String str, boolean isAll){
//		HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();  
//		hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
//		hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//		
//		if(str==null || str.length()==0)
//			return "";
//		StringBuilder pinyin=new StringBuilder();
//		
//		try {
//			String[] pinArray=null;
//			for(char c : str.toCharArray()){
//				if(String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")){  
//					pinArray = PinyinHelper.toHanyuPinyinStringArray(c, hanYuPinOutputFormat);
//					if(pinArray!=null && pinArray.length>0){
//						if(isAll)
//							pinyin.append(pinArray[0]);
//						else
//							pinyin.append(pinArray[0].charAt(0));
//					}
//				}else{
//					pinyin.append(c);
//				}
//			}
//		} catch (BadHanyuPinyinOutputFormatCombination e) {
//			e.printStackTrace();
//		}
//		return pinyin.toString();
		return null;
	}
}
