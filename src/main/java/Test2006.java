import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

public class Test2006 {

	public static void main(String[] args) throws Exception {
		readEnExcelFile();
	}

	private static void readEnExcelFile() throws Exception {
		ExcelReader reader = ExcelUtil.getReader("d:/en.xlsx");
		List<Map<String, Object>> readAll = reader.readAll();

		System.out.println(readAll.size());

		try (PrintWriter fout = new PrintWriter("d:/out.txt", "ISO-8859-1")) {
			readAll.forEach(row -> {
				fout.println(row.get("key").toString().trim() + "=" + row.get("value").toString().trim());
			});
			fout.flush();
		}
		System.out.println("==========");
	}
}
