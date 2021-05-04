import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Test2021 {
	
	public static void main(String[] args) throws Exception  {
//		String[] aa = "D:\\temp\\pic\\一口钟\\0648-一口钟-001.jpg".split("\\\\");
//		System.out.println(aa);
		
//		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
//		System.out.println(UUID.randomUUID().toString().replace("-", ""));
		
//		String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
//		String encryptedData = "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZM" + "QmRzooG2xrDcvSnxIMXFufNstNGTyaGS"
//				+ "9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+" + "3hVbJSRgv+4lGOETKUQz6OYStslQ142d"
//				+ "NCuabNPGBzlooOmB231qMM85d2/fV6Ch" + "evvXvQP8Hkue1poOFtnEtpyxVLW1zAo6"
//				+ "/1Xx1COxFvrc2d7UL/lmHInNlxuacJXw" + "u0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn"
//				+ "/Hz7saL8xz+W//FRAUid1OksQaQx4CMs" + "8LOddcQhULW4ucetDf96JcR3g0gfRK4P"
//				+ "C7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB" + "6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns"
//				+ "/8wR2SiRS7MNACwTyrGvt9ts8p12PKFd" + "lqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYV"
//				+ "oKlaRv85IfVunYzO0IKXsyl7JCUjCpoG" + "20f0a04COwfneQAGGwd5oa+T8yO5hzuy" + "Db/XcxxmK01EpqOyuxINew==";
//		String iv = "r7BXXKkLb8qrSNn05n0qiA==";
//		System.out.println(decodeWxMessage(sessionKey, encryptedData, iv));
		
		test0425();
	}
	
	public static void test0425() throws Exception {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sitedb", "cnmedicinedb",
				"cnMedicine@pg2020#!");
		
		try (PrintWriter fout = new PrintWriter("d:/out.txt", "utf-8")) {
			ResultSet rst = conn.createStatement().executeQuery("select medicine_name, medicine_content from medicine_info;");
			
			while(rst.next()) {
				String drugName = rst.getString("medicine_name").trim();
				String content = rst.getString("medicine_content").trim();
				String pinyin = converterToSpell(drugName);
				
				Map<String, String> infoMap = gson.fromJson(content, type);
				String otherName = StringUtils.isNoneBlank(infoMap.get("别名")) ? infoMap.get("别名").trim() : "";
				String latin = StringUtils.isNoneBlank(infoMap.get("拉丁名称")) ? infoMap.get("拉丁名称").trim() : "";
				String yaoxing = StringUtils.isNoneBlank(infoMap.get("药性")) ? infoMap.get("药性").trim() : "";
				boolean poisonFlag = yaoxing.indexOf("毒") > -1;
				String usage = StringUtils.isNoneBlank(infoMap.get("用法用量"))
						? infoMap.get("用法用量").replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("\n", " ")
						: "";

				System.out.println(drugName);
				System.out.println(pinyin);
				System.out.println(otherName);
				System.out.println(latin);
				System.out.println(yaoxing);
				System.out.println(poisonFlag);
				System.out.println(usage);
				System.out.println("================================\n");
				
				StringBuilder sql = new StringBuilder();
				sql.append("update cat_drug set spell_abbr='").append(pinyin).append("', name_alias='").append(otherName);
				sql.append("', name_eng='").append(latin).append("', poisonous_flag='").append(poisonFlag?"1":"0");
				sql.append("', doseage_desc='").append(usage).append("' where name_chn='").append(drugName).append("'; \n");
				
				fout.print(sql.toString());
			}
		}
	}
	
	private  static String converterToSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].substring(0, 1);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
	
	public static void test0418() throws Exception {
		System.out.println(MD5.create().digestHex("1"));
		System.out.println(MD5.create().digestHex("mz1234"));
		System.out.println(String.format("%.2f", new BigDecimal("2300.0000")));
		
		List<String> idList = new ArrayList<>();
		idList.add("2345-sdfgh-235");
//		idList.add("rtyu-sdfgh-235");
		
		String ss = String.join(",",
				idList.stream().map(id -> "'" + id + "'").collect(Collectors.toList()));
		System.out.println(ss);
		
		
		String[] userArr = {};
		userArr = ss.split(",");
		System.out.println(userArr.length);
		
//		if (smsInfo.get("allSalerUser") != null) {
//			userArr = smsInfo.get("allSalerUser").split(",");
//		}
		
	}
	public static void test0414() throws Exception {
		
		//============导入医院企业数据SQL，以名称剔除数据库中已经存在===================================================================================
//		try (BufferedReader sysin = new BufferedReader(new FileReader("d:/sys.txt"));
//				BufferedReader newin = new BufferedReader(new FileReader("d:/new.txt"))) {
//			
//			Set<String> nameSet = new HashSet<>();
//			sysin.lines().forEach(line->{
//				nameSet.add(line.trim());
//			});
//			
//			newin.lines().filter(line->!nameSet.contains(line.trim()) && StringUtils.isNotBlank(line)).forEach(line->{
//				String id = UUID.randomUUID().toString();
//				StringBuilder sql = new StringBuilder();
//				sql.append("INSERT INTO cnmedicinedb.cat_org(id, org_type, org_name, org_abbr, sms_right_flag, create_user, create_username, create_date)");
//				sql.append("\n");
//				sql.append("VALUES('").append(id).append("', '1', '").append(line.trim()).append("','").append(line.trim()).append("','0', '7b8fd953-87e6-42c9-81ba-322f4deb9e05', '管理员', now());");
//				sql.append("\n");
//				sql.append("INSERT INTO cnmedicinedb.cat_enterprise(id, manufacture_flag, create_user, create_date)");
//				sql.append("\n");
//				sql.append("VALUES('").append(id).append("', '1', '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
//				sql.append("\n");
//				
//				System.out.println(sql.toString());
//				
//			});
//		}
		
		//================处理产品数据，以 产品名称+规格+企业名称 作为唯一判定===============================================================================
//		Map<String, String> unitIdMap = new HashMap<>();
//		Map<String, String> orgIdMap = new HashMap<>();
//		
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/data-db", "cnmedicinedb",
//				"cnMedicine@pg2020#!");
//
//		ResultSet rst = conn.createStatement().executeQuery("select id, metric_name_chn from cat_metric;");
//		while(rst.next()) {
//			String id = rst.getString("id");
//			String name = rst.getString("metric_name_chn").trim();
//			unitIdMap.put(name, id);
//		}
//		
//		rst.close();
//		rst = conn.createStatement().executeQuery("select id, org_name from cat_org;");
//		while(rst.next()) {
//			String id = rst.getString("id");
//			String name = rst.getString("org_name").trim();
//			orgIdMap.put(name, id);
//		}
//		
//		try (BufferedReader in = new BufferedReader(new FileReader("d:/product.txt"));PrintWriter fout = new PrintWriter("d:/out.txt","utf-8")){
//			Set<String> checkSet = new HashSet<>();
//			AtomicInteger count = new AtomicInteger(0);
//			
//			in.lines().filter(line->StringUtils.isNotBlank(line)).forEach(line->{
//				String[] arr = line.split("#");
//				
//				String id = UUID.randomUUID().toString();
//				String productName = arr[0];
//				String spec = arr[1];
//				String unitName = arr[2];
//				String unitId = unitIdMap.get(arr[2]);
//				String orgName = arr[3];
//				String orgId = orgIdMap.get(arr[3]);
//				
//				String key = productName+"#"+spec+"#"+orgName;
//				
//				if(!checkSet.contains(key)) {
//					checkSet.add(key);
//					
//					StringBuilder sql = new StringBuilder();
//					sql.append("INSERT INTO cnmedicinedb.cat_product");
//					sql.append("(id, product_name, product_spec, metric_id, metric_name, manufacture_id, manufacture_name, create_user, create_username, create_date)");
//					sql.append("\n");
//					sql.append("VALUES('").append(id).append("','").append(productName).append("','").append(spec).append("','").append(unitId).append("','").append(unitName);
//					sql.append("','").append(orgId).append("','").append(orgName).append("', '7b8fd953-87e6-42c9-81ba-322f4deb9e05', '管理员', now());");
//					sql.append("\n");
//					
//					fout.print(sql.toString());
//					System.out.print(count.addAndGet(1)+" >> ");
//					System.out.println(sql.toString());
//				}
//			});
//		}
		
	
		
		
		
		//================处理his企业对照表SQL===============================================================================
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
//				"cnMedicine@pg2020#!");
//
//		Map<String, String> orgIdMap = new HashMap<>();
//		ResultSet rst = conn.createStatement().executeQuery("select id, org_name  from cat_org ;");
//		while(rst.next()) {
//			String id = rst.getString("id");
//			String name = rst.getString("org_name").trim();
//			orgIdMap.put(name, id);
//		}
//		
//		try (BufferedReader in = new BufferedReader(new FileReader("d:/new.txt"));PrintWriter fout = new PrintWriter("d:/out.txt","utf-8")){
//			AtomicInteger count = new AtomicInteger(0);
//			Set<String> checkSet = new HashSet<>();
//			
//			in.lines().filter(line->StringUtils.isNotBlank(line)).forEach(line->{
//				
//				String id = UUID.randomUUID().toString();
//				String orgName = line.trim();
//				String orgId = orgIdMap.get(orgName);
//				
//				if(!checkSet.contains(orgName)) {
//					checkSet.add(orgName);
//					
//					StringBuilder sql = new StringBuilder();
//					sql.append("INSERT INTO cnmedicinedb.face_his_saler_map");
//					sql.append("(id, buyer_orgid, org_id, his_saler_full_name, factory_flag, remark)");
//					sql.append("\n");
//					sql.append("VALUES('").append(id).append("','35484549-017f-45aa-bf2b-fac389b32a43','").append(orgId).append("','");
//					sql.append(orgName).append("','1','数据导入');");
//					sql.append("\n");
//					
//					fout.print(sql.toString());
//					System.out.print(count.addAndGet(1)+" >>\t");
//					System.out.println(sql.toString());
//				}
//				
//			});
//		}
		
		
		//================处理his产品对照表SQL===============================================================================
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
//				"cnMedicine@pg2020#!");
//
//		try (PrintWriter fout = new PrintWriter("d:/out.txt","utf-8")){
//			AtomicInteger count = new AtomicInteger(0);
//			Set<String> checkSet = new HashSet<>();
//			ResultSet rst = conn.createStatement().executeQuery("select id, product_name, product_spec, manufacture_id, manufacture_name  from cat_product ;");
//			while(rst.next()) {
//				String id = UUID.randomUUID().toString();
//				
//				String product_id = rst.getString("id");
//				String product_name = rst.getString("product_name").trim();
//				String product_spec = rst.getString("product_spec").trim();
//				String manufacture_id = rst.getString("manufacture_id").trim();
//				String manufacture_name = rst.getString("manufacture_name").trim();
//				
//				StringBuilder sql = new StringBuilder();
//				sql.append("INSERT INTO cnmedicinedb.face_his_product_map");
//				sql.append("(id, buyer_orgid, product_id, manufacture_id, his_product_name, his_medical_spec, his_factory_name, remark)");
//				sql.append("\n");
//				sql.append("VALUES('").append(id).append("','35484549-017f-45aa-bf2b-fac389b32a43','").append(product_id).append("','");
//				sql.append(manufacture_id).append("','").append(product_name).append("','").append(product_spec).append("','").append(manufacture_name).append("', '数据导入');");
//				sql.append("\n");
//				
//				fout.print(sql.toString());
//				System.out.print(count.addAndGet(1)+" >>\t");
//				System.out.println(sql.toString());
//				
//			}
//		}
		
		//================处理合同明细表SQL===============================================================================
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
//				"cnMedicine@pg2020#!");
//		
//		String contractId = "9b8cc6dc-1a49-4b57-bb61-db38ec5fe921";
//		int stand_rate = 1;
//		String begin_date = "2021-04-01";
//		String end_date = "2021-12-31";
//		
//		Map<String, Map<String, String>> productMap = new HashMap<>();
//		
//		Map<String, String> orgIdMap = new HashMap<>();
//		orgIdMap.put("广东一方制药有限公司","114291cd-0811-4861-93e5-4c5578b86d31");       
//		orgIdMap.put("广东振宁医药集团贸易公司","5c458f9d-6cd6-4afd-80ec-99832a83b884");
//		orgIdMap.put("广东深华药业有限公司","148a9f42-777f-4c99-970d-18a319a4d5ab");
//		orgIdMap.put("广东恒祥医药有限公司","ee175e86-bf11-406c-8b18-7572d93f6e6e");
//		orgIdMap.put("国药控股梅州有限公司","1d1c2844-e62c-46e1-870a-8759540c653f");
//		orgIdMap.put("广州医药有限公司","0d67a72c-9821-487e-ba55-582ab68777b5");
//		orgIdMap.put("南阳医乐嘉艾草制品有限公司","83aa0d24-1f57-40d9-b747-fc43b04d1759");
//		orgIdMap.put("江西国药有限责任公司","6977096d-828d-4f79-875a-ec4f8143def8");
//		orgIdMap.put("辽宁恒源堂药业有限公司","4c684660-9aa4-44e4-a588-fed7f16eebea");
//		
//		ResultSet rst = conn.createStatement().executeQuery("select id, product_name, product_spec, manufacture_name, manufacture_id, quality_name, plant_area, metric_name from cat_product where create_username like '丰顺中医院%' ;");
//		while(rst.next()) {
//			String product_id = rst.getString("id");
//			String product_name = rst.getString("product_name").trim();
//			String product_spec = rst.getString("product_spec").trim();
//			
//			String manufacture_name = rst.getString("manufacture_name");
//			String manufacture_id = rst.getString("manufacture_id");
//			
//			String quality_name = rst.getString("quality_name").trim();
//			String plant_area = rst.getString("plant_area").trim();
//			String metric_name = rst.getString("metric_name").trim();
//			
//			String key = product_name+"#"+product_spec+"#"+plant_area;
//			
//			Map<String, String> row = new HashMap<>();
//			row.put("product_id", product_id);
//			row.put("product_name", product_name);
//			row.put("product_spec", product_spec);
//			row.put("manufacture_name", manufacture_name);
//			row.put("manufacture_id", manufacture_id==null?"":manufacture_id);
//			row.put("quality_name", quality_name);
//			row.put("plant_area", plant_area);
//			row.put("metric_name", metric_name);
//			
//			productMap.put(key, row);
//		}
//
//		try (BufferedReader in = new BufferedReader(new FileReader("d:/newproduct.txt"));
//				PrintWriter fout = new PrintWriter("d:/out.txt", "utf-8")) {
//			AtomicInteger count = new AtomicInteger(0);
//			Set<String> checkSet = new HashSet<>();
//
//			in.lines().filter(line -> StringUtils.isNotBlank(line)).forEach(line -> {
//
//				String[] arr = line.split("#");
//				
//				String id = UUID.randomUUID().toString();
//				
//				String productName = arr[0];
//				String spec = arr[1];
//				//String price = arr[2];
////				String orgName = arr[3];
//				String plantArea = arr[4];
//				String salerName = arr[5];
//				String price = arr[6];
//				String salerId = orgIdMap.get(salerName);
//
//				String key = productName + "#" + spec + "#" + plantArea;
//				
//				if(!checkSet.contains(key)) {
//					checkSet.add(key);
//					
//					System.out.println("==========="+key);
//					Map<String, String> row = productMap.get(key);
//					
//					StringBuilder sql = new StringBuilder();
//					sql.append("INSERT INTO cnmedicinedb.con_list_item");
//					sql.append("(id, contract_id, hit_price, temp_price, status_flag, product_id, product_name, saler_orgid, saler_name, saler_easy, ");
//					sql.append("sender_orgid, sender_name, sender_easy, manufacture_id, manufacture_name, manufacture_easy, quality_name, product_spec, ");
//					sql.append("stand_rate, begin_date, end_date, plant_area, metric_name, create_user, create_date)");
//					sql.append("\n");
//					sql.append("VALUES('").append(id).append("','9b8cc6dc-1a49-4b57-bb61-db38ec5fe921',").append(price).append(",").append(price).append(",'1','");
//					sql.append(row.get("product_id")).append("','").append(row.get("product_name")).append("','").append(salerId).append("','").append(salerName).append("','").append(salerName).append("','");
//					sql.append(salerId).append("','").append(salerName).append("','").append(salerName);
//					
//					if(StringUtils.isBlank(row.get("manufacture_id"))) {
//						sql.append("', null, null, null, '");
//					}else {
//						sql.append("','").append(row.get("manufacture_id")).append("','").append(row.get("manufacture_name")).append("','").append(row.get("manufacture_name")).append("','");
//					}
//					
//					sql.append(row.get("quality_name")).append("','").append(spec).append("', 1, '2021-04-01', '2021-12-31', '").append(row.get("plant_area")).append("','");
//					sql.append(row.get("metric_name")).append("', '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
//					sql.append("\n");
//					
//					fout.print(sql.toString());
//					System.out.print(count.addAndGet(1)+" >> ");
//					System.out.println(sql.toString());
//				}
//
//			});
//		}
		

		
		
		//================处理ord_hit_comm表SQL===============================================================================
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
				"cnMedicine@pg2020#!");
		
		try (PrintWriter fout = new PrintWriter("d:/out.txt", "utf-8")) {
			AtomicInteger count = new AtomicInteger(0);
			ResultSet rst = conn.createStatement().executeQuery("select *  from con_list_item where id in ('6c956b76-f84f-470c-9219-278f37112be0','8a2caf57-e073-4ce8-80f3-e3301ce1e99a','32521b77-8340-4b1d-85fe-a5aa37881e7a','4c35985a-1e85-462f-8afa-8920dc8e0bee','c48a1d50-e9a3-42bd-841d-37feabde4444','13557bb3-bd8a-4da3-8db6-f283ccabccc7','f357914d-fb4e-48cd-9bce-8c9ed23d3b1e','78e67951-f8c2-44ee-9bd8-1b80bb0860b0','578a5715-fa25-4c7e-91ab-a7bd52f2f7c3','1f75be91-816f-447d-9f7a-5d579d5952a7','5f1a8126-17c7-4117-8f80-db1d98edde4a','18a4fcfc-f198-4d75-9361-4e67053690b5','4bcaf5e7-5b51-4bc5-91a7-a59d0f080e1a','9f818bb7-50f3-41ec-8767-12fce33f3e54','e4720cf1-a16c-47c2-bfd8-25f44c60ec5c','f26811b3-0904-40cd-b387-8a1844243f6f','06f782e3-8b17-4027-9f42-361f8ea9f93c','46ed1c70-f12a-4bf0-8f9d-7d9514da26a7','0246ded6-0e94-4f14-88b4-70e523068684','355487af-893b-4dcd-9396-c70a120af2af','9e61f771-36a1-47d5-8228-ae71e94a150b','13949335-a461-48dd-899f-2cfbec6dc6b8','ddc8f292-67e5-4aad-bf7d-32821cefccae','2f14d2a2-9574-44c3-9e35-0a489cc2601d','06d0dbc5-0b7d-4fc0-8f8a-91f020515e48','03f74e3f-e08f-4071-bfdd-84c15fdac1d8','9c1d3ce7-f45d-45f9-af1a-f34fea989254','b90ce332-b402-4faa-9126-10c21f71fdf6','17b56b1d-cad8-4fa5-8e27-e1835bdaa98f','854bb3a4-a701-4e28-8527-b0ff907ad23f','c51ce698-d5cd-46ad-994f-1c56efbd7e13','e35d6a5b-b1ff-4079-a4df-b20580338221','65adbeeb-3997-4454-a100-5ef84abf26f2','1367cf5f-ff40-4f67-a2c8-50e45436b839','c54c292e-fde7-4c01-9f5f-41f13fd93dc1','f56c783c-055c-43c2-8210-478134ab567c','76a01904-22d6-4ae3-9064-8204bf3943c9','f07e6314-af08-472e-8780-c14b63e94cf5','29804831-e9eb-4820-84ef-c1982cbca4b9','532c6d0c-b598-4682-8cb2-f6416513e180','f1d397ca-d9fe-4626-9d5e-e78efa7d9d60','1e30d137-18b6-4bd9-9bf6-0fdeabae0eb2','0c9dd3c9-6aaa-4a10-927b-ba609b7c2725','e469e057-554f-4f42-a068-db611f6313e8','cac3bc15-9eef-4f61-b603-13fd17972a55','7cf2064e-3444-4d43-a153-cedfb0c9240c','a9063045-64b6-41ef-85fd-dba98a6f2d07','ddd185e1-4bc8-4a20-a7b0-1b473cb03d8d','f92a03b5-dd1e-435d-9c4f-8710d4b58e23','73664dc2-6404-4554-98c0-dcccd7fc39cb','3b881b5d-574f-4754-b9b4-44a2ad24e2aa','02f57e63-21e7-4e47-b50c-f18d49b05420','5fc5cff8-3284-4eab-bc8d-3d5752f3fb57','fa1c1b8b-358d-4a6d-81e5-b525b41ab4b1','7fce7a2e-f8bc-40cd-b8b8-84b181fd3ff9','3579465c-3899-4c68-9dc0-758b457502a3','b5c7562f-f6da-4760-9c03-444388118c7d','09067cb3-98af-4dff-bda2-cbd30dd86c70','c19cedd4-da37-4fe3-9924-92995e37ea90','296ef3da-1390-4323-9083-be1976c0a545','59b53cc4-b429-4ec3-b49c-49b9ff91cf1c','3838e9f2-ce9e-4ee5-9c59-f07b33f60eb4','7dcc5749-b500-48b5-8834-1248e43976d1','2a329f92-ea75-466f-8e95-3a80b98d3bb1','f31a9a94-57ab-4217-a847-5b71448d95f9','e457c025-5c3e-42b0-9239-b3061fd7e3a1','507ee794-db36-4f04-b15a-d11a8348d939','f0d71fe1-a50e-404e-b996-a82affce1717','cebcd08d-feaa-4fc0-ac8f-8c83c4987bde','f607dcb5-3102-42b7-8ac5-6f43cd5d0958','597176d8-c949-41e6-b685-e883e35a2954','7f2b8090-979d-4ec3-a45e-266f1cf8ef4b','29e3789e-ae20-45fb-8845-f8369cd275ed','91519465-e95b-413a-8b8b-702ee7aa2a1b','7a31b803-bc88-496f-99b4-2ddc60cc95ca','be5f77b7-9424-4957-8be8-73b4ac8ba0d7','f0993d9b-9b68-4802-8523-01dceb0f1e57','ce103e55-8fec-4d3f-85a8-a276d950064b','4135bf14-131c-48ab-9bac-aa33a47dca01','20d4ade4-af2c-4f0e-8b4f-176b89782c09','d302cfec-d4d6-4704-a71f-087957963051','55c72755-72c7-474e-9d7a-64917c9e4068','02a0f2a2-bf26-4a2f-8aa5-2d90cc76405c','b4a64dd3-d551-4f65-a449-578d79fba8c1','a277b464-90a5-44f9-9010-47bcc50469c0','b7054fd6-b485-4405-bbcb-00d195e850c5','14a7eb42-4aa3-4fb0-8923-3883ef5a9179','cd7ce681-865d-4bec-ac5a-64bfe7afad28','2eb86428-139b-40e7-9f00-12df3117af00','a41ab50d-aaba-4906-8b6b-66dc66c9f491','2de06651-a26e-4bc5-98ca-cf6abfc263e6','7f5fdaa0-c86f-4974-a7b0-d87babf17ed5','e937dc15-9154-42c6-9352-ae5072d72f29','47cf0af7-0345-4210-b9ff-57162e7c7c31','69a5e0bc-a19a-4962-a21b-da23bf7c25c8','428f2752-42e0-4b07-8818-abe5c0488378','0c876d95-2f0b-49d5-b539-ab6b79a0abc4','e77e5f19-2012-4996-aea3-0a6f001fd326','8a8678e5-4d4c-482a-8d32-c351effddfd2','aeed3c6a-6819-4395-9ed8-334268bc72db','bda40727-c0ad-44f2-8bae-ca13ee64f778','522e3e85-4f2f-4fb3-af6d-ee9af0a29f13','2254c758-1083-4af5-84bc-8da398fcb888','2dec4321-4d52-4443-a164-e27d9fc26598','9a859be3-15e4-4435-95ce-fd3fad779816','c96c88e2-c7b6-44d6-b1f7-1594e8d58c26','5f0fdc19-23ea-4791-8978-4054c6bf01d1','6afb9d93-30b0-4066-a913-228016be6eee','0f9eb60b-3508-4ac8-93e0-a8e584005df0','651261f3-254c-418b-b16c-bdba7ae5ccfe','dab3b004-6629-437d-9398-84744326baa7','52c55170-bc2e-4daf-88f4-003ed4e8d629','8bc1f51c-67de-484e-985b-5bea052a1f60','42ad8814-1a81-4bd4-8076-33df00b88b5b','13762dc9-d14c-4d02-82ea-1bf8e8d5890c','f3b8ac93-f9d6-4ad5-9362-1353252a30ef','e33a21a3-f87b-40de-8285-49c8a9b0ddd6','796b8fa4-33c1-4d66-b362-9cf712b1df75','e64c0365-b080-43bf-80e1-eec34f4e1518','2c540bf9-4673-4404-aa2a-341a657adc11','a565241e-526d-4d4b-bf08-d57e8b31d508','c2ea66de-9025-4f5b-aa58-07ef9de736d8','a1b0d6cc-e73f-4096-8590-c0e1bb47debc','9fd6cf66-5178-4479-9c8d-859ed010cc10','f6c08d37-bd08-4b3f-b031-880ce7e6cea1','7d3545a1-f07c-4d8b-b71c-6a936a131fee','b5c7a996-78ee-4a7f-90f8-a9f8605eb0d0','e2f8609e-818c-4999-81f8-fd157145a6cc','7aa8a950-fe58-426d-9685-702d7fd0b55b','15789e69-1761-4cf2-a1bf-f847ed38fa37','7735bdfb-a2c1-4cd2-aef0-dce18ddc9c6d','2dd73265-cd5a-4cbd-b216-1623b33f1e8a','349dd69c-f01f-4dcd-82a4-406eb372b7cd','248d0b5d-2586-4105-8bc3-882e7f623cdc','004280a1-4278-427d-99f1-3a381244bb7b','6457dfdd-c7af-41cb-a162-6d6181c2e5aa','50c347af-72ce-40a1-aa5f-124a4945789b','01727dbd-c738-471a-bc21-a2d0dc68098d','f73e2d94-71ef-4426-abd5-43059ae5dda3','117a6388-dbc4-454b-8ff4-fc31ad0cdc9f','9f6c6e00-bd1f-4a50-b3c4-be42e4d46e50','3fffc37d-7b23-4f62-9ab8-e9ac3f771af8','21637991-a95c-45c4-b835-f6c35afe04f4','d7b7c327-f4bf-4430-9cf2-a28ae77f18f2','3e3a606b-f567-47fc-8fdc-92b77c6a45a8','10a868ec-681e-498a-9fd3-e5b849fdd3a9','92a3c92e-7c25-4446-885e-2fb9834afe85','d1f42667-6767-46f5-bd7d-6a37c6319506','ded6eea9-b7fd-4320-8b85-48be54d82f10','90f8fb17-6081-464c-a0ee-3d5d8ff739e9','16f6906c-7fe6-40fe-ab56-3c0bba8d3367','21c1ce8c-ee00-483b-9aa5-bc0f525b5197','ab6092dd-94b6-4cc0-8355-adf47faf6e88','d2f95ddd-4a07-4706-8c16-2f43c671862f','ade86624-5ac8-4790-a501-bafbf10dc010','3db5c31e-eda5-4814-986c-28420f338100','81ff61b7-782c-4849-96fa-ce67e906168f','089946d6-0a5d-4b0f-b4cf-08efabeccc79','2e0f0c54-2a5a-44c2-a225-5500e32e6d76','2017a8c5-caf3-491d-8c0e-c8e0080f43d3','a34cce81-d776-4ec6-970d-1c646cc5fc65','ae66bb2d-7aca-4328-b268-53daddd7e95e','9acc7799-bf5c-4d44-876e-288a8b828674','c26cb1cd-7433-43ee-9b26-6387de6daebc','684a0a11-8959-41b8-9d99-6017c7869c8b','30ed80c4-eb7b-4268-ad3b-b41ef94ca73a','b43ef417-3685-46c9-9c1f-c9a81198d0c6','a219896b-8bbc-4cba-880e-ead37493a8d9','82960db8-de3a-49d7-b580-4f5b71c97cfe','679705a6-003f-45fd-b7df-6feb3ba7676e','f77d6871-4ce2-46d1-9d47-74fa07952166','c4a06975-083c-4b39-ab19-27ef81f13391','1ff0f10e-3759-4e35-a9c6-931ba9ae9209','1675d84b-e1ed-4e3d-a0b2-c5117998cb3b','c035161d-37b8-4d3a-9526-43d6a91ec64c','d3f76866-2fa9-4b2a-9f40-7e58d77b2267','164088ce-a88f-4a21-b17e-cbf1781a7f07','b86debb2-b11d-4a6e-a35c-f17335c37543','86bec2f7-4752-4d55-a0ac-f70f367b82b6','26538af8-179f-48b8-b685-a2e4d489f792','ebbe4670-4304-4bce-9d57-8dec225bc26c','8a59d999-a82a-4047-b692-c3c4972474e1','1b7a5f01-1dec-43d8-ae00-fd03fd94ac56','84f42776-f977-4146-b25d-c74039916e94','90eae382-fad7-4edd-9efb-3bf10b4f6498','6af1b7a8-1063-46fc-9b49-12e4d3327d6d','61673736-581f-4125-833b-892b8ef54a8f','9a732d25-20d5-4e5f-a18e-db17ddfd6331','a7c92904-a710-461a-a3af-f32afeb03da7','288ef673-beb2-4457-809a-5e7110faed74','dc7effac-0c20-4ec6-85b8-3ee49c745a1f','7d6df41c-2d66-4a33-8f71-f3eba5ae4914','71c1b9af-977d-4cf0-8a5c-101ea7ea3f70','96832ed4-657e-4105-98a5-3e319f032d90','8efaa3ae-8c59-4d7b-aa26-8c4a29c6b18f','f68aa676-3112-42f5-b4a3-f2a908b63e6a','a73c3b74-1e3b-461b-973f-5a8c03acad0f','c0213a91-f669-4eaf-bfe0-c1fbe92a3449','342f729e-1ca1-4353-8bd8-2dcd09f72747','2c831a35-29f6-4c8b-9575-9aa14a42fb0d','76f7ff72-5706-472e-a14f-7cdba457c5a4','bf6789a9-d6ba-40f7-a190-96985dd17624','af1171da-b324-415b-9ec8-f266710cb918','46ae7a8c-ee79-4b5c-a07e-80106564aed6','f4f06530-1d3d-49f6-a951-b22df70bff1f','64c2e7ed-726d-4ff5-9d14-1e616b9e6f48','3eaac362-da75-47c8-8f92-5289a8e5c6a4','5a9d3fa7-d438-40a0-b874-7c06c33b49f4','20a1333f-ca68-4ca9-aa88-bcb0776cfecd','5866aca9-6b9e-479f-a615-bd88439608e0','e8765d6d-553f-41e1-accb-e35b9c20b253','a2563ca0-17dd-4813-8654-a60d5f1b0ff2','ee916897-ae53-42ee-8ccf-c1ed9a8f4938','91e459fd-943e-437e-bc17-6c748fff38e3','eb445674-bc01-425c-830f-5991b636be7f','07d9f41d-84c8-4ede-91ce-27b92d243503','f27e88a8-e527-4097-afd6-1c4ef770f375','4fda98be-5a27-41a0-a091-78ac0602267b','ffe4570d-66fc-42b4-b022-880607e99d7e','e29f139b-eea3-42f0-a887-230b4310a246','9df4a02f-0759-4fe5-9957-88361e9d3c66','f310981f-8967-43cf-a1f2-04ee26419e1e','64d03dfc-b10e-48d2-bbed-47b7a52fcfe3','52f3644b-ff86-4373-90e7-80b4fd5f2787','aff62dc4-8f08-4e37-8615-cd9459e64bed','ba9b4219-6f2f-4a99-9db9-55379093c105','8243c38b-704b-4222-935b-8325ebe67d2d','2d5ab392-1e77-4df7-be8d-67e3120ba4cc','9a6d69db-9f28-4fac-a3d6-a9e6d4507a85','128393b5-ba4a-4445-a093-25fd234264c2','8684d61f-f3c6-42f0-ab17-907ac0fe81a3','c331dfb7-9d65-41f8-9d37-422a2e70b669','6ae9044b-fc32-4dd1-bede-dd5b6aef1ba7','7b7b0a5c-20b9-4e65-8eef-45cf478c77f4','477ed497-3d97-476b-9759-978b2a0c0a10','62fc8510-88df-41b0-b265-b460b8b36660','9e0f1f22-d439-480a-b622-204309e7237a','78b9aba1-0d1d-4a31-8cf1-e2fbea6b9c20','419699dd-4f5f-4385-97bb-8e33618ac9db','f0e5fe7d-8885-4c69-b40c-df29a9cedf66','ad15d830-767a-43ed-9afb-037b24396fd5','705627c9-e25a-4d79-a1f1-a9aba69ef878','a4f2df1f-cda8-42a8-8c32-9c104d7e9392','36f953d6-5f31-4b08-88e8-54a0bb453f60','c6d36b7c-a3b7-4e8c-8f35-7de7c393f56d','08128f78-a47c-45e6-96f8-678144651318','623cd914-1cc6-4dec-8b04-8af108d0b112','7758c5ab-5ef0-4b70-96ac-7780d38865cc','ba852c06-357b-4fa2-b9b5-fe0fb17ab52f','6741bfc5-6c18-4263-9dc0-fa6de34e4829','a081dd53-cbde-4ee4-bb0c-2c2d170827c3','9c39085a-3fbf-4b28-ab4b-a05ed6af64c7','58f72b19-c553-45d7-84bb-ee175db75a49','76ce764a-e064-4ccb-9ddc-aac3d679d8b4','c7a2038c-9000-45b1-b4ab-e5cb6693ec4a','33abb70c-2deb-4420-bbf2-7d1b5af0209d','5a9197f8-dc9e-4cad-8fb6-ace9241c28be','03383b9a-8db9-4095-8c91-ebc105f46698','0e990995-3025-41d3-8a06-0234c46f98c5','95fd3d22-b0a3-4926-8347-41ce34f6d970','714003f8-0978-4850-85d6-b4f5b9acebea','b3b5606b-0fd3-4ecd-8d40-c9e636b77391','ce086c63-9c8c-413a-93c5-472fa600bdc1','c620f38a-b6b6-4700-a880-c22b7e780726','b937d430-b8f8-49dc-8be5-cfbf10daccaa','247032d8-e19c-4116-8e0c-195745b35758','51353f98-b403-46ec-a967-354f66a77d94','e817c779-a0d4-439c-8ddc-c7022139f2e8','74365845-5835-4dcd-9ef1-72296ad8f6d9','05838220-1d5b-4e5c-b90e-ef242af79853','05a72116-4cb7-42b5-8bce-16ea5c322a2a','4ad1daa6-7fc9-4aa1-8064-85be302209f8','bd8f60c6-dfe1-4dca-a969-7099ca501fcd','5fea9c10-2d3f-4a93-ac27-1c6ae643c449','60f39785-7b2b-45c4-acb0-885d1c8a0549','8fdf1a50-c916-482d-8e9b-b38dce14cf11','6473be2e-635a-4f2e-8fd3-c0647b242619','4534bce5-421c-4aae-a94b-de94822ddd0f','b8deda63-529a-4b2f-8a4d-5cd92ab3753e','cbf395ed-78ad-454a-96e3-1f57d10ed1cc','a299de32-76d4-46aa-b810-277b22d73ee6','b88884e4-267f-4ad7-95d6-7f1555a2cba8','e5792ed0-94ce-413c-8bac-e3a3bd7fce2e','79e54ec7-3162-4c1f-9d16-b785e3634d25','0349531a-6338-4487-aa81-2f46cc6ff9fa','642b53f2-0fe4-428a-9fa1-2170abbe37fa','76e8a1f1-59de-46c9-8331-672dacd45c7a','276539f1-cab0-4155-a6cc-95d3323c4281','32660d46-f351-4cc5-8e7a-551e18a482fb','47d9efb7-9ac4-4f79-88aa-c287d4f1a9f4','182d8e12-d9af-444f-8f77-6efe04c35608','3f838fa6-3343-4c00-a1c5-2492cc7c9145','01017b44-edf6-4973-9806-368e38bb6404','1c8595f4-7879-45cd-9db5-8aca9cb05c91','76cd359b-825c-4964-a3ed-7086b63abad0','beb533b8-ce99-4fa9-90d6-85c073e056c8','85b295ef-780a-472a-b530-a8fabe0f62ba','c020cede-78f1-4704-8e0f-73c38ea79bd0','5dddae83-1bbb-4c7b-b769-39f2b75bb2d7','18ea0f41-38df-45db-a889-873c777cfa4d','2540d894-0f96-4696-87ed-5f8da0970391','5c9f14f4-d9b6-4790-ac69-1790694176af','d54e3874-5c59-4446-8c36-e1cf4e877d3f','bd46885f-68a4-4c3d-82c3-185177df94d6','7aa354c0-3eea-476b-97c5-f20e85968188','56e5be9c-091b-4540-aaed-11e0935f61f3','a4a9f911-3961-4667-81f9-8c818dee4b91','4c989ed0-c958-4769-b51b-533899c2651c','de6eaad8-868f-4a96-9f0e-62b171896763','00798944-8add-4b72-92f0-caa3400f8e44','da9a253a-945c-431a-9c25-adc65e359094','2c79e2d5-2544-4f15-9375-60d9c92066eb','80899640-56a7-49cc-8dba-0fcb1fb8a891');");
			while(rst.next()) {
				String id = UUID.randomUUID().toString();
				
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO cnmedicinedb.ord_hit_comm");
				sql.append("(id, plat_id, contract_id, contract_type, contract_item_id, buyer_orgid, bak_buyer_fullname, bak_buyer_shortname, ");
				sql.append("saler_orgid, bak_saler_fullname, bak_saler_shortname, sender_orgid, bak_sender_fullname, bak_sender_shortname, ");
				sql.append("manufacture_orgid, bak_manufacture_fullname, bak_manufacture_shortname, product_id, bak_product_name, ");
				sql.append("plant_area, hit_price, temp_price, bak_drug_spec, bak_spec_unit, ");
				sql.append("bak_mass_assignment, bak_stand_spec_rate, create_user, create_date)");
				sql.append("\n");
				sql.append("VALUES('").append(id).append("', 'c43a3a38-9799-4c4f-9ed3-ca7316859ade', '9b8cc6dc-1a49-4b57-bb61-db38ec5fe921', '2', '");
				sql.append(rst.getString("id")).append("', 'ed1c605a-dc2e-4517-9156-f0ac0e511ded', '丰顺县中医院', '丰顺县中医院', '");
				sql.append(rst.getString("saler_orgid")).append("','").append(rst.getString("saler_name")).append("','").append(rst.getString("saler_easy")).append("','");
				sql.append(rst.getString("sender_orgid")).append("','").append(rst.getString("sender_name")).append("','").append(rst.getString("sender_easy"));
				
				if(StringUtils.isBlank(rst.getString("manufacture_id"))) {
					sql.append("', null, null, null, '");
				}else {
					sql.append("','").append(rst.getString("manufacture_id")).append("','").append(rst.getString("manufacture_name")).append("','").append(rst.getString("manufacture_name")).append("','");
				}
				
				sql.append(rst.getString("product_id")).append("','").append(rst.getString("product_name")).append("','");
				sql.append(rst.getString("plant_area")).append("',").append(String.format("%.2f", rst.getBigDecimal("hit_price"))).append(",").append(String.format("%.2f", rst.getBigDecimal("temp_price"))).append(",'");
				sql.append(rst.getString("product_spec")).append("','").append(rst.getString("metric_name")).append("','");
				sql.append(rst.getString("quality_name")).append("', 1, '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
				
				sql.append("\n");
				
				sql.append("INSERT INTO cnmedicinedb.ord_hit_comm_price");
				sql.append("(id, ord_hit_comm_id, product_id, saler_orgid, hit_price, create_user, create_date)");
				sql.append("\n");
				sql.append("VALUES('").append(UUID.randomUUID().toString()).append("','").append(id).append("','").append(rst.getString("product_id")).append("','");
				sql.append(rst.getString("sender_orgid")).append("',").append(String.format("%.2f", rst.getBigDecimal("hit_price")));
				sql.append(", '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
				
				sql.append("\n");
				sql.append("\n");
				
				fout.print(sql.toString());
				System.out.print(count.addAndGet(1)+" >> ");
				System.out.println(sql.toString());
			}
		}
		
		
		
		
		
		
//		Map<String, String> orgIdMap = new HashMap<>();
//		orgIdMap.put("广东一方制药有限公司","114291cd-0811-4861-93e5-4c5578b86d31");       
//		orgIdMap.put("广东振宁医药集团贸易公司","5c458f9d-6cd6-4afd-80ec-99832a83b884");
//		orgIdMap.put("广东深华药业有限公司","148a9f42-777f-4c99-970d-18a319a4d5ab");
//		orgIdMap.put("广东恒祥医药有限公司","ee175e86-bf11-406c-8b18-7572d93f6e6e");
//		orgIdMap.put("国药控股梅州有限公司","1d1c2844-e62c-46e1-870a-8759540c653f");
//		orgIdMap.put("广州医药有限公司","0d67a72c-9821-487e-ba55-582ab68777b5");
//		orgIdMap.put("南阳医乐嘉艾草制品有限公司","83aa0d24-1f57-40d9-b747-fc43b04d1759");
//		orgIdMap.put("江西国药有限责任公司","6977096d-828d-4f79-875a-ec4f8143def8");
//		orgIdMap.put("辽宁恒源堂药业有限公司","4c684660-9aa4-44e4-a588-fed7f16eebea");
//		
//		Map<String, String> unitIdMap = new HashMap<>();
//		unitIdMap.put("公斤","424f2a9d-8334-40e7-9b65-1da846c74b45");  
//		unitIdMap.put("盒","879ec9fc-08f1-406e-8ea7-3ddd284e9a1a");  
//		unitIdMap.put("份","10b86a2a-3cbf-495c-b5fc-1f9dbcc69346");  
//		unitIdMap.put("条","63259f05-57b2-4254-beba-84c1d180d7a3");  
//		
//		try (BufferedReader in = new BufferedReader(new FileReader("d:/newproduct.txt"));
//				PrintWriter fout = new PrintWriter("d:/out.txt", "utf-8")) {
//			AtomicInteger count = new AtomicInteger(0);
//			Set<String> checkSet = new HashSet<>();
//
//			in.lines().filter(line -> StringUtils.isNotBlank(line)).forEach(line -> {
//
//				String[] arr = line.split("#");
//
//				String id = UUID.randomUUID().toString();
//				String productName = arr[0];
//				String spec = arr[1];
//				String metricName = arr[2];
//				String orgName = arr[3];
//				String planArea = arr[4];
//				String salerName = arr[5];
//				String price = arr[6];
//				
//				String orgId = orgIdMap.get(orgName);
//				String salerId = orgIdMap.get(salerName);
//				String metricId = unitIdMap.get(metricName);
//
//				if("N".equals(orgName)) {
//					orgName="";
//				}
//				String key = productName + "#" + spec + "#" + orgName + "#" + planArea;
//
//				if (!checkSet.contains(key)) {
//					checkSet.add(key);
//					
//					StringBuilder sql = new StringBuilder();
//					sql.append("INSERT INTO cnmedicinedb.cat_product");
//					sql.append("(id, product_name, product_spec, plant_area,quality_id,quality_name, metric_id, metric_name, manufacture_id, manufacture_name, create_user, create_username, create_date)");
//					sql.append("\n");
//					sql.append("VALUES('").append(id).append("','").append(productName).append("','").append(spec).append("','").append(planArea);
//					sql.append("', '5153201a-c5b4-49e2-ad2c-42da95115ffd','一级', '").append(metricId).append("','").append(metricName);
//					
//					if(StringUtils.isBlank(orgName)) {
//						sql.append("',null,null,");
//					}else {
//						sql.append("','").append(orgId).append("','").append(orgName).append("',");
//					}
//					
//					sql.append("'7b8fd953-87e6-42c9-81ba-322f4deb9e05', '丰顺中医院数据导入', now());");
//					sql.append("\n");
//					
//					fout.print(sql.toString());
//					System.out.print(count.addAndGet(1)+" >> ");
//					System.out.println(sql.toString());
//				}
//
//			});
//		}
		
		
		
		
		
		
		
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
//				"cnMedicine@pg2020#!");
//
//		try (PrintWriter fout = new PrintWriter("d:/out.txt","utf-8")){
//			AtomicInteger count = new AtomicInteger(0);
//			ResultSet rst = conn.createStatement().executeQuery("select id, product_name, product_spec, manufacture_id, manufacture_name, plant_area  from cat_product where create_username like '丰顺中医院%';");
//			while(rst.next()) {
//				String id = UUID.randomUUID().toString();
//				
//				String product_id = rst.getString("id");
//				String product_name = rst.getString("product_name").trim();
//				String product_spec = rst.getString("product_spec").trim();
//				String manufacture_id = rst.getString("manufacture_id");
//				String manufacture_name = rst.getString("manufacture_name");
//				String plant_area = rst.getString("plant_area").trim();
//				
//				StringBuilder sql = new StringBuilder();
//				sql.append("INSERT INTO cnmedicinedb.face_his_product_map");
//				sql.append("(id, buyer_orgid, product_id, his_product_name, his_medical_spec, manufacture_id, his_factory_name, his_plant_area, remark)");
//				sql.append("\n");
//				sql.append("VALUES('").append(id).append("','ed1c605a-dc2e-4517-9156-f0ac0e511ded','").append(product_id).append("','");
//				sql.append(product_name).append("','").append(product_spec);
//				
//				if(StringUtils.isBlank(manufacture_id)) {
//					sql.append("', null, null, '");
//					
//				}else {
//					sql.append("','").append(manufacture_id).append("','").append(manufacture_name).append("','");
//				}
//				
//				sql.append(plant_area).append("', '数据导入');");
//				sql.append("\n");
//				
//				fout.print(sql.toString());
//				System.out.print(count.addAndGet(1)+" >>\t");
//				System.out.println(sql.toString());
//				
//			}
//		}
		
		
		
		
		
		
	}
	
	public static void test0410() {
		String content = "晒干或低温干燥。\u003c/p\u003e \\n\u003cp\u003e\u003cstrong\u003e【性状】\u003c/strong\u003e";
		content = content.replace('\u003c', '<');
		content = content.replace('\u003e', '>');
		
		System.out.println(content);
	}
	public static void test0302() {
		double sum = 12.3456783;
		BigDecimal tt = new BigDecimal(sum).setScale(2,RoundingMode.HALF_UP);
		System.out.println(tt);
		System.out.println(LocalDate.now().getMonthValue());
		System.out.println(LocalDate.now().getYear());
		System.out.println(SecureUtil.sha256("1"));
	}

	public static void test03() {
//		OkHttpClient client = new OkHttpClient();
//		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx282692b60e64c51b&secret=1387fbd351021a2a09c0ed758196f641&js_code=073vV40w36r5XV2N1z2w3YXtxX1vV40Y&grant_type=authorization_code";
//		Request request = new Request.Builder().url(url).build();
//		try (Response response = client.newCall(request).execute()) {
//			System.out.println(response.body().string());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		String xmlStr = "<xml>"
				+ "  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>"
				+ "  <attach><![CDATA[支付测试]]></attach>"
				+ "  <bank_type><![CDATA[CFT]]></bank_type>"
				+ "  <fee_type><![CDATA[CNY]]></fee_type>"
				+ "  <is_subscribe><![CDATA[Y]]></is_subscribe>"
				+ "  <mch_id><![CDATA[10000100]]></mch_id>"
				+ "  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>"
				+ "  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>"
				+ "  <out_trade_no><![CDATA[1409811653]]></out_trade_no>"
				+ "  <result_code><![CDATA[SUCCESS]]></result_code>"
				+ "  <return_code><![CDATA[SUCCESS]]></return_code>"
				+ "  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>"
				+ "  <time_end><![CDATA[20140903131540]]></time_end>"
				+ "  <total_fee>1</total_fee>"
				+ "  <coupon_fee><![CDATA[10]]></coupon_fee>"
				+ "  <coupon_count><![CDATA[1]]></coupon_count>"
				+ "  <coupon_type><![CDATA[CASH]]></coupon_type>"
				+ "  <coupon_id><![CDATA[10000]]></coupon_id>"
				+ "  <trade_type><![CDATA[JSAPI]]></trade_type>"
				+ "  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>"
				+ "</xml>";
		
		
		xmlStr = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid><out_trade_no><![CDATA[1409811653]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><coupon_fee><![CDATA[10]]></coupon_fee><coupon_count><![CDATA[1]]></coupon_count><coupon_type><![CDATA[CASH]]></coupon_type><coupon_id><![CDATA[10000]]></coupon_id><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		
		Document docResult = XmlUtil.parseXml(xmlStr);
		
		System.out.println("appid: " + docResult.getElementsByTagName("appid").item(0).getTextContent());
		System.out.println("result_code: " + docResult.getElementsByTagName("result_code").item(0).getTextContent());
		System.out.println("out_trade_no: " + docResult.getElementsByTagName("out_trade_no").item(0).getTextContent());
		System.out.println("time_end: " + docResult.getElementsByTagName("time_end").item(0).getTextContent());
		
//		System.out.println(docResult.getChildNodes().item(0).getChildNodes().getLength());
		
		NodeList nodeList = docResult.getFirstChild().getChildNodes();
		for(int i=0;i<nodeList.getLength();i++) {
			Node node = nodeList.item(i);
			System.out.println(node.getNodeName()+"====="+node.getTextContent());
		}
	}
	
	public static String decodeWxMessage(String sessionKey, String encryptedData, String iv) {
		 // 被加密的数据
       byte[] dataByte = Base64.decode(encryptedData);
       // 加密秘钥
       byte[] keyByte = Base64.decode(sessionKey);
       // 偏移量
       byte[] ivByte = Base64.decode(iv);
       try {
           // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
           int base = 16;
           if (keyByte.length % base != 0) {
               int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
               byte[] temp = new byte[groups * base];
               Arrays.fill(temp, (byte) 0);
               System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
               keyByte = temp;
           }
           // 初始化
           Security.addProvider(new BouncyCastleProvider());
           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
           AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
           parameters.init(new IvParameterSpec(ivByte));
           cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
           byte[] resultByte = cipher.doFinal(dataByte);
           if (null != resultByte && resultByte.length > 0) {
               String result = new String(resultByte, "UTF-8");
               return result;
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       
       return "";
	}
}
