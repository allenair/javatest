package com.cnmedicine;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

public class UpdateProdactData {
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/data-db";

	private static Connection getConnect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_URL, "cnmedicinedb", "cnMedicine@pg2020#!");

		return conn;
	}

	public static void main(String[] args) throws Exception{
		genUpdateProductSql("d:/cat_product修改加编码20210907.xlsx");

	}

	private static void genUpdateProductSql(String fileName) throws Exception {
		Map<String, String> productDrugMap = new HashMap<String, String>();
		ResultSet rst = getConnect().createStatement().executeQuery("SELECT cp.id, cp.drug_id  FROM cat_product cp ");
		while (rst.next()) {
			productDrugMap.put(rst.getString("id"), rst.getString("drug_id"));
		}

		ExcelReader reader = ExcelUtil.getReader(fileName);
		List<Map<String, Object>> allMap = reader.readAll();

		try (PrintWriter fout = new PrintWriter("d:/product-drug.sql", "utf-8");) {
			for (Map<String, Object> row : allMap) {
				String productId = row.get("id").toString().trim();
				String drugName = row.get("drug_name").toString().trim();
				String isYibao = row.get("是否医保产品").toString().trim().equals("是") ? "1" : "0";
				String productCode = row.get("产品编码").toString().trim();

				String productSql = String.format(
						"UPDATE cat_product SET drug_name='%s', insure_flag='%s', product_code='%s' WHERE id='%s';\n",
						drugName, isYibao, productCode, productId);
				fout.print(productSql);
				
				String drugSql = String.format("UPDATE cat_drug SET name_chn='%s' WHERE id='%s';\n\n", drugName, productDrugMap.get(productId));
				fout.print(drugSql);
			}

		}
	}
}
