package com.cnmedicine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class MZDealData {

	public static void main(String[] args) throws Exception  {
		genNewProductSql();

	}
	
	private static void genNewProductSql() throws Exception  {
		String buyerName = "梅州市中医院";
		String productName = "黄连颗粒";
		String spec = "600g";
		String metricUnit = "袋";
		String manufactureName = "广东一方制药有限公司";
		String salerName = "广东一方制药有限公司";
		BigDecimal price = new BigDecimal("426.0");
		String plantArea = "无";
		
		
		//----------------------------------------------------------------------
		String productId = UUID.randomUUID().toString();
		String faceMapId = UUID.randomUUID().toString();
		String conItemId = UUID.randomUUID().toString();
		String hitCommId = UUID.randomUUID().toString();
		String hitCommPriceId = UUID.randomUUID().toString();
		
		String conId = "";
		String platId = "";
		String beginDate = "";
		String endDate = "";
		
		String buyerId = "";
		String salerId = "";
		String manufactureId = "";
		String metricId = "";
		
		//----------------------------------------------------------------------
		Connection conn = getConn();
		ResultSet rst = conn.prepareStatement("select * from con_list cl where cl.status_flag='1';").executeQuery();
		if(rst.next()) {
			conId = rst.getString("id");
			platId = rst.getString("plat_id");
			beginDate = rst.getDate("begin_date").toString();
			endDate = rst.getDate("end_date").toString();
		}else {
			System.out.println("=======con_list empty===============");
			return;
		}
		
		
		PreparedStatement pst = conn.prepareStatement("select * from cat_metric cm where cm.metric_name_chn=?;");
		pst.setString(1, metricUnit);
		rst = pst.executeQuery();
		if(rst.next()) {
			metricId = rst.getString("id");
		}else {
			System.out.println("=======metric empty==============");
			return;
		}
		
		
		pst = conn.prepareStatement("select * from cat_org co where co.org_name=?;");
		
		pst.setString(1, buyerName);
		rst = pst.executeQuery();
		if(rst.next()) {
			buyerId = rst.getString("id");
		}else {
			System.out.println("=======buyer empty=======");
			return;
		}
		
		pst.setString(1, salerName);
		rst = pst.executeQuery();
		if(rst.next()) {
			salerId = rst.getString("id");
		}else {
			System.out.println("=======saler empty=======");
			return;
		}
		
		pst.setString(1, manufactureName);
		rst = pst.executeQuery();
		if(rst.next()) {
			manufactureId = rst.getString("id");
		}else {
			System.out.println("=======manufacture empty=======");
			return;
		}
		
		
		
		//----------------------------------------------------------------------
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO cnmedicinedb.cat_product(id, product_name, product_spec, plant_area,");
		sql.append("quality_id,quality_name, metric_id, metric_name, manufacture_id, manufacture_name, create_user, create_username, create_date)");
		sql.append("\n");
		sql.append("VALUES('").append(productId).append("','").append(productName).append("','").append(spec).append("','").append(plantArea);
		sql.append("', '5153201a-c5b4-49e2-ad2c-42da95115ffd','一级', '").append(metricId).append("','").append(metricUnit).append("','");
		sql.append(manufactureId).append("','").append(manufactureName).append("','7b8fd953-87e6-42c9-81ba-322f4deb9e05', '").append(buyerName).append("数据导入', now());");
		
		System.out.println("--------------cat_product-----data和trade都需要执行---------");
		System.out.println(sql.toString());
		System.out.println();
		
		//----------------------------------------------------------------------
		sql = new StringBuilder();
		sql.append("INSERT INTO cnmedicinedb.face_his_product_map(id, buyer_orgid, product_id, manufacture_id, his_product_name, his_medical_spec, his_factory_name, remark)");
		sql.append("\n");
		sql.append("VALUES('").append(faceMapId).append("','").append(buyerId).append("','").append(productId).append("','").append(manufactureId).append("','");
		sql.append(productName).append("','").append(spec).append("','").append(manufactureName).append("', '数据导入');");
		
		System.out.println("--------------face_his_product_map--------------");
		System.out.println(sql.toString());
		System.out.println();
		
		//----------------------------------------------------------------------
		sql = new StringBuilder();
		sql.append("INSERT INTO cnmedicinedb.con_list_item(id, contract_id, hit_price, temp_price, status_flag, product_id, product_name, ");
		sql.append("saler_orgid, saler_name, saler_easy, sender_orgid, sender_name, sender_easy, manufacture_id, manufacture_name, manufacture_easy, quality_name, ");
		sql.append("product_spec, stand_rate, begin_date, end_date, plant_area, metric_name, create_user, create_date) ");
		sql.append("\n");
		sql.append("VALUES('").append(conItemId).append("','").append(conId).append("',").append(price.toString()).append(",").append(price.toString()).append(",'1','");
		sql.append(productId).append("','").append(productName).append("','").append(salerId).append("','").append(salerName).append("','").append(salerName).append("','");
		sql.append(salerId).append("','").append(salerName).append("','").append(salerName).append("','").append(manufactureId).append("','").append(manufactureName).append("','");
		sql.append(manufactureName).append("', '一级', '").append(spec).append("', 1, '").append(beginDate).append("','").append(endDate).append("','").append(plantArea).append("','");
		sql.append(metricUnit).append("', '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
		
		System.out.println("--------------con_list_item--------------");
		System.out.println(sql.toString());
		System.out.println();
		
		//----------------------------------------------------------------------
		sql = new StringBuilder();
		sql.append("INSERT INTO cnmedicinedb.ord_hit_comm(id, plat_id, contract_id, contract_type, contract_item_id, buyer_orgid, bak_buyer_fullname, ");
		sql.append("  bak_buyer_shortname, saler_orgid, bak_saler_fullname, bak_saler_shortname, sender_orgid, bak_sender_fullname, bak_sender_shortname, ");
		sql.append("  manufacture_orgid, bak_manufacture_fullname, bak_manufacture_shortname, product_id, bak_product_name, plant_area, hit_price, temp_price, ");
		sql.append("  bak_drug_spec, bak_spec_unit, bak_mass_assignment, bak_stand_spec_rate, create_user, create_date)");
		sql.append("\n");
		sql.append("VALUES('").append(hitCommId).append("','").append(platId).append("','").append(conId).append("', '2', '").append(conItemId).append("','");
		sql.append(buyerId).append("','").append(buyerName).append("','").append(buyerName).append("','").append(salerId).append("','").append(salerName).append("','").append(salerName).append("','");
		sql.append(salerId).append("','").append(salerName).append("','").append(salerName).append("','").append(manufactureId).append("','");
		sql.append(manufactureName).append("','").append(manufactureName).append("','").append(productId).append("','").append(productName).append("','");
		sql.append(plantArea).append("',").append(price.toString()).append(",").append(price.toString()).append(",'").append(spec).append("','").append(metricUnit).append("', '一级', 1, '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
		
		System.out.println("--------------ord_hit_comm--------------");
		System.out.println(sql.toString());
		System.out.println();
		
		//----------------------------------------------------------------------
		sql = new StringBuilder();
		sql.append("INSERT INTO cnmedicinedb.ord_hit_comm_price(id, ord_hit_comm_id, product_id, saler_orgid, hit_price, create_user, create_date)");
		sql.append("\n");
		sql.append("VALUES('").append(hitCommPriceId).append("','").append(hitCommId).append("','").append(productId).append("','").append(salerId).append("',");
		sql.append(price.toString()).append(", '7b8fd953-87e6-42c9-81ba-322f4deb9e05', now());");
		
		System.out.println("--------------ord_hit_comm_price--------------");
		System.out.println(sql.toString());
		System.out.println();
		
		
		System.out.println("--------------在系统上配置相应药品后执行以下语句----------------------------");
		
		//----------------------------------------------------------------------
		sql = new StringBuilder();
		sql.append("update cat_product set drug_name=(select d.name_chn from cat_drug d where d.id=drug_id) where id='").append(productId).append("'; \n");
		sql.append("update cat_product set drug_name_pinyin=(select d.spell_abbr from cat_drug d where d.id=drug_id) where id='").append(productId).append("'; \n");
		sql.append("update cat_product set doseage_id=(select d.doseage_form_id from cat_drug d where d.id=drug_id) where id='").append(productId).append("'; \n");
		sql.append("update cat_product set doseage_name=(select d.name_chn from cat_doseage_form d where d.id=doseage_id) where id='").append(productId).append("'; \n");
		System.out.println("--------------data和trade都需要执行---------");
		System.out.println(sql.toString());
		System.out.println();
		
		sql = new StringBuilder();
		sql.append("update con_list_item set drug_id=(select p.drug_id from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		sql.append("update con_list_item set drug_name=(select p.drug_name from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		sql.append("update con_list_item set doseage_name=(select p.doseage_name from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		System.out.println(sql.toString());
		System.out.println();
		
		sql = new StringBuilder();
		sql.append("update ord_hit_comm set bak_drug_id=(select p.drug_id from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		sql.append("update ord_hit_comm set bak_drug_name=(select p.drug_name from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		sql.append("update ord_hit_comm set bak_drug_pinyin=(select p.drug_name_pinyin from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		sql.append("update ord_hit_comm set bak_drug_code=(select p.drug_code from cat_drug p where p.id=bak_drug_id) where product_id='").append(productId).append("'; \n");
		sql.append("update ord_hit_comm set bak_drug_dosage=(select p.doseage_name from cat_product p where p.id=product_id) where product_id='").append(productId).append("'; \n");
		System.out.println(sql.toString());
		System.out.println();
		
		
	}

	
	private static Connection getConn() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trade-db", "cnmedicinedb",
				"cnMedicine@pg2020#!");
		
		return conn;
	}
	
	
	
}
