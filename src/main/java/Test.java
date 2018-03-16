import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mongodb.util.Hash;
import com.zxtech.RedisUtil;
import com.zxtech.bean.UpHardAnalysisVO;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Test tt = new Test(); 
		
		String zhongwen = "这是测试";
		Map<String, String> map = new HashMap<>();
		map.put("abc", zhongwen);
		map.put("xyz", "123");
		System.out.println(new Gson().toJson(map));
		
		zhongwen = URLEncoder.encode(zhongwen, "UTF-8");
		map.put("abc", zhongwen);
		String json = new Gson().toJson(map);
		System.out.println(json);
		
		
		System.out.println(URLDecoder.decode(json, "UTF-8"));
		
		String ss = "00000000010000010000000001111111000000001000001010111001000000000000000000101100000000010000000000000000000000010000000000000000000000001000010010000000000000010000111100101101001011010000000000000110000000000000000000000000011011000100000111110111000000000010001011111111000000000010100111100100000000010000000011111111111111111111111111111111000000000000000000000000000000000010000000000001000000000000000001000000000000000000000001100100000000000000000000000000000000000000000000000000000000000000000001110010";
		System.out.println("ss:>>"+Base64.getEncoder().encodeToString(getByteByStr(ss)));
		
		
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = new HashMap<>();
		
		map1.put("1", "1");
		map1.put("11", "11");
		map1.put("111", "111");
		map1.put("1111", "1111");
		
		map2.put("a", "a");
		map2.put("aa", "aa");
		map2.put("aaa", "aaa");
		map2.put("aaaa", "aaaa");
		
		Set<String> mySet = map1.keySet();
		mySet.addAll(map2.keySet());
		
		for (String string : mySet) {
			System.err.println(string);
		}
	}
	
	
	
	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}
	
	private static byte[] getByteByStr(String str) {
		int len = str.length()/8;
		byte[] resArr = new byte[len];
		
		int tmp = 0;
		for(int i=0; i<len; i++) {
			tmp = 0;
			tmp += Integer.parseInt(str.substring(i*8, i*8+1))<<7;
			tmp += Integer.parseInt(str.substring(i*8+1, i*8+2))<<6;
			tmp += Integer.parseInt(str.substring(i*8+2, i*8+3))<<5;
			tmp += Integer.parseInt(str.substring(i*8+3, i*8+4))<<4;
			tmp += Integer.parseInt(str.substring(i*8+4, i*8+5))<<3;
			tmp += Integer.parseInt(str.substring(i*8+5, i*8+6))<<2;
			tmp += Integer.parseInt(str.substring(i*8+6, i*8+7))<<1;
			tmp += Integer.parseInt(str.substring(i*8+7, i*8+8));
			resArr[i] = (byte)tmp;
		}
		return resArr;
	}

	private void getPlanAndRealCount(String startDate, String endDate, String line_id, int interval_day) {
		String key="EmployeePerformanceDaoImpl#getPlanAndRealCount@"+startDate+"&"+endDate+"&"+line_id+"&"+interval_day;
		System.out.println(key);
	}
	public void redisTool() {
		int fl = 1;
		UpHardAnalysisVO bean = new UpHardAnalysisVO();
		
		while (true) {
			int up = getRandom(2);
			int down = getRandom(2);

			if (up == 1 && down == 1) {
				if (getRandom(2) == 1) {
					up = 0;
				} else {
					down = 0;
				}
			}

			if (up == 1) {
				fl++;
				fl=fl>6?6:fl;
			}
			if (down == 1) {
				fl--;
				fl=fl<1?1:fl;
			}

			bean.setUp("" + up);
			bean.setDown("" + down);
			
			bean.setFl(fl);
			Gson gson = new Gson();
			RedisUtil.set("hard-analy:900000000000000080006", gson.toJson(bean));
			try{
				Thread.sleep(1010);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private int getRandom(int scope){
		return new Random().nextInt(scope);
	}
	
	public int getIntByString(String str){
		int len = str.length();
		int byteNum = len/8;
		StringBuilder sb = new StringBuilder();
		
		for(int i=byteNum-1; i>=0; i--){
			sb.append(str.substring(i*8, (i+1)*8));
		}
		
		return Integer.parseInt(sb.toString(), 2);
	}
	
	public String getParameterBitValue(String baseParam){
		StringBuilder strBuffer = new StringBuilder();
		byte[] srcArr = Base64.getDecoder().decode(baseParam);
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
		return strBuffer.toString();
	}
	
	public String getMonthAndDay(String startDate, String endDate) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar tmp = start;
		start.setTime(format.parse(startDate));
		end.setTime(format.parse(endDate));
		
		int monthCount=0;
		while(true){
			tmp.add(Calendar.MONTH, 1);
			if(end.compareTo(tmp)>0){
				tmp.add(Calendar.MONTH, -1);
				break;
			}
			monthCount++;
		}
		
		int tmpMonth = tmp.get(Calendar.MONTH)+1;
		int tmpDay = tmp.get(Calendar.DAY_OF_MONTH);
		int endMonth = end.get(Calendar.MONTH)+1;
		int endDay = end.get(Calendar.DAY_OF_MONTH);
		int dayCount = 0;
		if(tmpMonth==endMonth){
			dayCount = end.get(Calendar.DAY_OF_MONTH) - tmp.get(Calendar.DAY_OF_MONTH) + 1;
		}else{
			if(endMonth==4 || endMonth==6 || endMonth==9 || endMonth==11){
				if(tmpDay==30){
					monthCount++;
				}else{
					
				}
			}else if(endMonth==2){
				
			}
		}
		
		
		int endYear = end.get(Calendar.YEAR);
		
		if(endYear%4==0 && endYear%100!=0 || endYear%400==0){
			
		}
		
		
		return "";
	}

	
	public static final TypeAdapter<java.sql.Date> DATE = new TypeAdapter<java.sql.Date>() {
        @Override
        public java.sql.Date read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(in.nextString()).getTime());
            } catch (Exception e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, java.sql.Date value) throws IOException {
            out.value(new SimpleDateFormat("yyyy-MM-dd").format(value));
        }
    };
	public static final TypeAdapter<java.sql.Time> TIME = new TypeAdapter<java.sql.Time>() {
        @Override
        public java.sql.Time read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return new java.sql.Time(new SimpleDateFormat("HH:mm:ss").parse(in.nextString()).getTime());
            } catch (Exception e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, java.sql.Time value) throws IOException {
            out.value(new SimpleDateFormat("HH:mm:ss").format(value));
        }
    };
//    
	public void test160812(){
		TestBean bean = new TestBean();
		bean.setFirstStr("aaa");
		bean.setMyDate(new java.sql.Date(System.currentTimeMillis()));
		bean.setMyTime(new Time(System.currentTimeMillis()));
		
		String aa = "{\"firstStr\":\"aaa\",\"myDate\":\"2016-12-08\",\"myTime\":\"\"}";
		GsonBuilder gsonBulder = new GsonBuilder();
        gsonBulder.registerTypeAdapter(java.sql.Date.class, DATE);   
        gsonBulder.registerTypeAdapter(java.sql.Time.class, TIME);   
        TestBean abean = gsonBulder.create().fromJson(aa, TestBean.class);
		
		
		System.out.println(abean);
	}
	
	public int hex2Int(Object s){
		return Integer.parseInt(s.toString(), 16);
	}
	
	public static String byte2Str(byte b) {
		String tmp = Integer.toHexString(byte2int(b)).toUpperCase();
		return tmp.length() == 1 ? "0" + tmp : tmp;
	}
	
	public static int byte2int(byte b){
		int tmp = (int)b;
		tmp = tmp & 0xff;
		return tmp;
	}
	public void test1225(){
		int a = 257;
		byte b = (byte)a;
		
		System.out.println(a+"  "+Integer.toBinaryString(a));
		System.out.println(b+"  "+Integer.toBinaryString((int)b));
		
	}
	
	public void test0819(){
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.getMostSignificantBits());
		System.out.println(uuid.getLeastSignificantBits());
//		System.out.println(uuid.timestamp());
		System.out.println(uuid.toString());
	}
	
	public void test1104(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 3);
		cal.set(Calendar.HOUR, 11);
		
		Date firdate = new Date();
		Date secdate = cal.getTime();
		System.out.println(firdate);
		System.out.println(secdate);
		System.out.println(this.twoDaysNum(firdate, secdate));
	}
	
	private int twoDaysNum(Date firstDate, Date secondDate){
		int res = 0;
		long firstMill = firstDate.getTime();
		long secondMill = secondDate.getTime();
		long differ = firstMill > secondMill ? firstMill - secondMill : secondMill - firstMill;
		
		res = (int)Math.ceil(differ*1.0/(24*60*60*1000));
		return res;
	}
}
