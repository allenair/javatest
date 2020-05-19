import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Test2004 {

	public static void main(String[] args) {
		String ss = "12345.0";
		System.out.println(ss.substring(0, ss.indexOf('.')));
		System.out.println(""+(int)Float.parseFloat(ss));
		LocalDate initDate = LocalDate.of(2000, 1, 1);
		System.out.println(initDate.format(DateTimeFormatter.ofPattern("yyyy-MM")));
		
		int aa = 12;
		String subject = "日报填写提醒（" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) + "，累计" + aa + "天）";
		System.out.println(subject);
		
//		t0514();
	}
	
	private static void t0514() {
		String cloudReturnDecryptInfo = "{\"ResultData\":[{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19453,\"Value\":\"制动盘\",\"CodeType\":\"BJCode\",\"OrderId\":73,\"Code\":\"73\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19454,\"Value\":\"主驱动链轮\",\"CodeType\":\"BJCode\",\"OrderId\":74,\"Code\":\"74\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19455,\"Value\":\"梯级链轮\",\"CodeType\":\"BJCode\",\"OrderId\":75,\"Code\":\"75\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19456,\"Value\":\"轴承座\",\"CodeType\":\"BJCode\",\"OrderId\":76,\"Code\":\"76\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19457,\"Value\":\"附加制动器复位装置\",\"CodeType\":\"BJCode\",\"OrderId\":77,\"Code\":\"77\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19458,\"Value\":\"紧固件\",\"CodeType\":\"BJCode\",\"OrderId\":78,\"Code\":\"78\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19459,\"Value\":\"主驱动安装过渡板\",\"CodeType\":\"BJCode\",\"OrderId\":79,\"Code\":\"79\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19516,\"Value\":\"空开\",\"CodeType\":\"BJCode\",\"OrderId\":136,\"Code\":\"136\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"zh-CN\",\"AutoId\":19519,\"Value\":\"端子排\",\"CodeType\":\"BJCode\",\"OrderId\":139,\"Code\":\"139\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19640,\"Value\":\"HANDRAIL DRIVE CHAIN SPROCKET\",\"CodeType\":\"BJCode\",\"OrderId\":69,\"Code\":\"69\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19643,\"Value\":\"MAIN DRIVE SHAFT\",\"CodeType\":\"BJCode\",\"OrderId\":72,\"Code\":\"72\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19644,\"Value\":\"AUXILIARY BRAKE\",\"CodeType\":\"BJCode\",\"OrderId\":73,\"Code\":\"73\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19645,\"Value\":\"MAIN DRIVE CHAIN\",\"CodeType\":\"BJCode\",\"OrderId\":74,\"Code\":\"74\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19646,\"Value\":\"STEP CHAIN\",\"CodeType\":\"BJCode\",\"OrderId\":75,\"Code\":\"75\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19647,\"Value\":\"BEARING HOUSE\",\"CodeType\":\"BJCode\",\"OrderId\":76,\"Code\":\"76\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19648,\"Value\":\"AUXILIARY BRAKE RESET DEVICE\",\"CodeType\":\"BJCode\",\"OrderId\":77,\"Code\":\"77\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19649,\"Value\":\"FASTENERS\",\"CodeType\":\"BJCode\",\"OrderId\":78,\"Code\":\"78\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19650,\"Value\":\"MAIN DRIVE INSTALLATION TRANSITION PLATE\",\"CodeType\":\"BJCode\",\"OrderId\":79,\"Code\":\"79\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19707,\"Value\":\"AIR SWITH\",\"CodeType\":\"BJCode\",\"OrderId\":136,\"Code\":\"136\",\"Ex2\":\"\",\"Ex1\":\"\"},{\"Ex3\":\"Escalator\",\"Description\":\"NULL\",\"Language\":\"en-US\",\"AutoId\":19710,\"Value\":\"TERMINAL BLOCK\",\"CodeType\":\"BJCode\",\"OrderId\":139,\"Code\":\"139\",\"Ex2\":\"\",\"Ex1\":\"\"}],\"Error\":{\"Code\":0,\"Message\":\"\"},\"Success\":true}";
		ApiReturn resultBean = new Gson().fromJson(cloudReturnDecryptInfo, ApiReturn.class);
		resultBean.getResultData().forEach(cloudRow -> {
			List<String> diffColumnList = new ArrayList<>();

			Object a = cloudRow.get("CodeType");
			Object b = cloudRow.get("CodeType");
			String type = "string";
			System.out.println(compareFunction(a, b));

			a = cloudRow.get("OrderId");
			b = cloudRow.get("OrderId");
			type = "int";
			System.out.println(compareFunction(a, b));
			
			a = cloudRow.get("Description");
			b = cloudRow.get("Description");
			type = "string";
			System.out.println(compareFunction(a, b));

		});
	}
	
	private static boolean compareFunction(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		} else if (a == null && b != null) {
			return false;
		} else if (a != null && b == null) {
			return false;
		}

		return a.toString().equals(b.toString());

	}
	
	private static void t0513() {
		System.out.println(LocalDateTime.parse("2020-05-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		Map<String, String> columnTypeMap = new HashMap<>();
		columnTypeMap.put("AutoId", "int");
		columnTypeMap.put("CodeType", "string");
		columnTypeMap.put("Code", "string");
		columnTypeMap.put("OrderId", "int");
		columnTypeMap.put("Description", "string");
		columnTypeMap.put("Value", "string");
		columnTypeMap.put("Ex1", "string");
		columnTypeMap.put("Ex2", "string");
		columnTypeMap.put("Ex3", "string");
		columnTypeMap.put("Language", "string");
		System.out.println(columnTypeMap.keySet().stream().map(str -> "'" + str + "'").collect(Collectors.joining(",")));
	}

	private static void t0511() {
		BigDecimal res = new BigDecimal("1.969873").multiply(new BigDecimal(100)).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
		System.out.println(res);
		
		List<String> list = new ArrayList<>(10);
		
		list.add(1,"222");
		
	}
	private static void t0410() {
		LocalDate endDate = LocalDate.now().plusDays(-1);
		LocalDate startDate = LocalDate.of(endDate.getYear(), Month.JANUARY, 1);
		System.out.println(endDate);
		System.out.println(startDate);
		
		endDate = LocalDate.now();
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
		System.out.println(startDate);
		
		System.out.println(LocalDateTime.now());
	}
}

class ApiReturn {
	private Map<String, Object> Error;
	private Boolean Success;
	private List<Map<String, Object>> ResultData = new ArrayList<Map<String,Object>>();

	public Map<String, Object> getError() {
		return Error;
	}

	public void setError(Map<String, Object> error) {
		Error = error;
	}

	public Boolean getSuccess() {
		return Success;
	}

	public void setSuccess(Boolean success) {
		Success = success;
	}

	public List<Map<String, Object>> getResultData() {
		return ResultData;
	}

	public void setResultData(List<Map<String, Object>> resultData) {
		ResultData = resultData;
	}

}