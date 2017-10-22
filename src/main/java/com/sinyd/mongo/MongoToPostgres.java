package com.sinyd.mongo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoToPostgres {

	
	public static void main(String[] args)throws Exception {
		new MongoToPostgres().toPostgres();
	}

	public void toPostgres()throws Exception{
		Mongo mongo= new Mongo("192.168.1.229");
		DB db = mongo.getDB("allen_test");
		DBCollection coll = db.getCollection("test");
		
		Class.forName("org.postgresql.Driver");
		Connection con=DriverManager.getConnection("jdbc:postgresql://192.168.1.229:5432/test","postgres","postgresql");
		con.setAutoCommit(true);
		PreparedStatement pst = con.prepareStatement("insert into test_table (equip_id, monitor_id, dept_dir, real_vlaue, real_day, oid) values (?,?,?,?,?,?)");
		
		DBCursor ret = coll.find().sort(new BasicDBObject("_id",1));
		DBObject obj;
		int count=1;
		while(ret.hasNext()){
			obj = ret.next();
			pst.setString(1, obj.get("equipId").toString());
			pst.setString(2, obj.get("monitorId").toString());
			pst.setString(3, obj.get("dept_dir").toString());
			pst.setBigDecimal(4, new BigDecimal(obj.get("real_vlaue").toString()));
			pst.setString(5, obj.get("real_day").toString());
			pst.setString(6, obj.get("_id").toString());
			pst.executeUpdate();
//			pst.addBatch();
//			if(count%200==0){
//				pst.executeBatch();
//			}
			count++;
			System.out.println(obj.get("_id"));
		}
		
		pst.close();
		con.close();
	}
}
