import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class Test2009 {
	private static GsonBuilder gsonBulder = new GsonBuilder();
	public static final TypeAdapter<java.sql.Timestamp> TIMESTAMP = new TypeAdapter<java.sql.Timestamp>() {
        @Override
        public java.sql.Timestamp read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS").parse(in.nextString()).getTime());
            } catch (Exception e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, java.sql.Timestamp value) throws IOException {
            out.value(new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS").format(value));
        }
    };
    
    
	public static void main(String[] args) {
		gsonBulder.registerTypeAdapter(java.sql.Timestamp.class, TIMESTAMP);
		
		t1030();
		LocalDateTime ss = LocalDateTime.parse("2020-10-12" + " 00:00:00",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		System.out.println(ss.plusDays(1));
	}

	private static void t0912() {
		BigDecimal zz = new BigDecimal(12.3);
		System.out.println(zz.setScale(2,RoundingMode.HALF_UP).toString());
		
		BigDecimal aa = new BigDecimal(100);
		BigDecimal bb = new BigDecimal(20.5);
		
		System.out.println(aa.add(bb.negate()));
		
		LocalDate createDate = LocalDate.now().minusMonths(4).withDayOfMonth(1);
		System.out.println(createDate);
		
		LocalDateTime createTime = LocalDateTime.now().minusMonths(4).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
		System.out.println(createTime);
		
		System.out.println(String.format("%.2f", 5*100.0/7)+"%");
		
		System.out.println(String.format("%.2f", new BigDecimal("5.00").doubleValue()/new BigDecimal(7).doubleValue())+"%");
	}
	
	private static void t1030() {
		String ss = "{\"msg\":\"查询数据条数[1]\",\"code\":0,\"data\":[{\"id\":\"e4b2ac4f-5c84-46ce-9c2e-30336be20002\",\"platId\":\"6f1071e6-cb8e-47fa-8cec-4632f586760e\",\"drugId\":\"6ed0a53f-fded-438b-927a-053382811485\",\"doseageId\":\"61effdf6-415c-4462-8a90-72a7f356d433\",\"manufactureId\":\"caad816e-36c8-4904-afc8-f29323e011e1\",\"productCode\":\"0109-YP00010-1\",\"drugName\":\"西洋参\",\"productName\":\"0109-YP00010-1\",\"memberFlag\":\"0\",\"productSpec\":\"统\",\"priceType\":\" \",\"permitNumber\":\"0\",\"materialSource\":\" \",\"productSource\":\"0\",\"importDrugLicenceNumber\":\"0\",\"materialQualityName\":\" \",\"gmpFlag\":\"0\",\"highFlag\":\"0\",\"qualityName\":\"  \",\"patentFlag\":\" \",\"protectionFlag\":\" \",\"wrapId\":\"                                    \",\"newDrugFlag\":\" \",\"consignFlag\":\" \",\"consignCompanyName\":\"                                    \",\"metricId\":\"46fb4a70-68b9-434b-8b02-5c02b2cf1767\",\"checkFlag\":\" \",\"enableFlag\":\" \",\"pfilePlat\":\"                                    \",\"sourceCountryId\":\"                                    \",\"checkUser\":\"                                    \",\"lockFlag\":\" \",\"govProtect\":\" \",\"plantArea\":\"吉林\",\"prescriptionFlag\":\" \",\"metricName\":\"千克\",\"createUser\":\"                                    \",\"createOrg\":\"                                    \",\"lastUpdateUser\":\"d4c2f2a9-baf0-40d4-96c9-7be9a0391648\",\"lastUpdateDate\":\"2020-09-03T20:53:26.764\",\"lastUpdateOrg\":\"                                    \",\"deleteFlag\":\"0\",\"qualityId\":\"                                    \",\"manufactureName\":\"北京明辉恒通药业有限公司\"}],\"count\":1}";
		
		List<Map<String, Object>> json = gsonBulder.create().fromJson(ss,new TypeToken<List<Map<String, Object>>>() {
		}.getType());
		
		System.out.println(json);
	}
}
