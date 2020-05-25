import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class Test2005 {

	public static void main(String[] args) {
		t0521();
	}

	private static void t0519() {
		LocalDate today = LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY);

		System.out.println(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	private static void t0521() {
		List<String> keyList = new ArrayList<>();
		Map<String, Entity> entityMap = new HashMap<String, Entity>();
		
		try {
			String[] cnArr = {"d:/tmp/language.properties","d:/tmp/language_zh.properties","d:/tmp/language_zh_CN.properties"};
			
			for (String cnfile : cnArr) {
				Properties prop = new Properties();
				prop.load(new FileInputStream(cnfile));
				prop.keySet().forEach(key -> {
					String keyStr = key.toString();
					if (entityMap.get(keyStr) == null) {
						keyList.add(keyStr);
						entityMap.put(keyStr, new Entity());
					}

					String valueStr = prop.get(keyStr) == null ? "" : prop.get(keyStr).toString();
					if (StringUtils.isNotBlank(valueStr) && StringUtils.isBlank(entityMap.get(keyStr).getCnName())) {
						entityMap.get(keyStr).setCnName(valueStr);
					}
				});
			}
			
			String[] enArr = {"d:/tmp/language_en.properties","d:/tmp/language_en_US.properties"};
			for (String enfile : enArr) {
				Properties prop = new Properties();
				prop.load(new FileInputStream(enfile));
				prop.keySet().forEach(key -> {
					String keyStr = key.toString();
					if (entityMap.get(keyStr) == null) {
						keyList.add(keyStr);
						entityMap.put(keyStr, new Entity());
					}

					String valueStr = prop.get(keyStr) == null ? "" : prop.get(keyStr).toString();
					if (StringUtils.isNotBlank(valueStr) && StringUtils.isBlank(entityMap.get(keyStr).getEnName())) {
						entityMap.get(keyStr).setEnName(valueStr);
					}
				});
			}
			
			try(BufferedReader in = new BufferedReader(new FileReader("d:/tmp/old.csv"))){
				in.lines().forEach(line->{
					if(StringUtils.isNotBlank(line)) {
						String[] arr = line.split(",#@#,");
						if(arr.length>1) {
							String keyStr = arr[0];
							String valueStr = arr[1];

							if (entityMap.get(keyStr) == null) {
								keyList.add(keyStr);
								entityMap.put(keyStr, new Entity());
							}

							if (StringUtils.isNotBlank(valueStr)) {
								entityMap.get(keyStr).setOldEnName(valueStr);
							}
						}
					}
				});
			}
			
			try(PrintWriter fout = new PrintWriter("d:/out-gbk.csv","GBK")){
				fout.println("key,中文,英文,一期英文");
				keyList.forEach(key->{
					Entity bean = entityMap.get(key);
					StringBuilder sb = new StringBuilder();
					sb.append(key);
					sb.append(",").append(bean.getCnName());
					sb.append(",").append(bean.getEnName());
					sb.append(",").append(bean.getOldEnName());
					fout.println(sb.toString());
				});
				fout.flush();
			}

			System.out.println("=======END============");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Entity {
	private String cnName="";
	private String enName="";
	private String oldEnName="";

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getOldEnName() {
		return oldEnName;
	}

	public void setOldEnName(String oldEnName) {
		this.oldEnName = oldEnName;
	}

}
