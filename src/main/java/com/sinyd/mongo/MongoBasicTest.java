package com.sinyd.mongo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
/*
 * mongo的基础操作
 * */
public class MongoBasicTest {

	/**
	 * @param args
	 */
	private Mongo mongo;
	private DB db;
	private DBCollection coll;

	public static void main(String[] args) throws Exception {
		new MongoBasicTest().test();
	}

	public void test() throws Exception  {
		long start = System.currentTimeMillis();
//		singleInsertTest();
//		batchInsertTest();
//		copyTest();
//		copyDB();
//		updateTest();
//		analysisTest();
		allTest229();
		System.out.println(System.currentTimeMillis() - start);
	}

	private void allTest229()throws Exception {
		mongo = new Mongo("192.168.1.229");
		db = mongo.getDB("test");
		coll = db.getCollection("person");
		
//		DBObject obj = new BasicDBObject();
//		obj.put("test_name", "foo");
//		obj.put("test_age", 15);
//		obj.put("create_date", new Date());
//		obj.put("long_val", 12345L);
//		
//		coll.insert(obj);
		
		DBCursor ret = coll.find(new BasicDBObject("test_name", Pattern.compile("fo*") ), new BasicDBObject("test_name",1).append("create_date", 1));
		while(ret.hasNext()){
			System.out.println(ret.next());
		}
		
	}
	
	private void singleInsertTest()throws Exception  {
		mongo = new Mongo("192.168.1.219");
		db = mongo.getDB("test_db");
		coll = db.getCollection("test_collection");
		coll.drop();
		
		for (int i = 0; i < 100000; i++) {
			DBObject obj = new BasicDBObject();
			obj.put("index", i);
			obj.put("name", "allen_" + i);
			obj.put("date", new Date());
			coll.insert(obj);
		}
		// ===3344ms=======
	}
	
	private void batchInsertTest()throws Exception  {
		mongo = new Mongo("192.168.1.219");
		db = mongo.getDB("test_db");
		coll = db.getCollection("test_collection");
		coll.drop();
		
		List<DBObject> list = new LinkedList<DBObject>();
		for (int i = 0; i < 100000; i++) {
			DBObject obj = new BasicDBObject();
			obj.put("index", i);
			obj.put("name", "allen_" + i);
			obj.put("date", new Date());
			list.add(obj);
		}
		coll.insert(list);
		// ===1797ms=======
	}
	
	private void copyDB()throws Exception{
		Mongo m1 = new Mongo("192.168.1.229");
		DB db1 = m1.getDB("allen_test");
		DBCollection coll1 = db1.getCollection("test");
		
		Mongo m2 = new Mongo("localhost");
		DB db2 = m2.getDB("allen_db");
		DBCollection col2 = db2.getCollection("allen_col");
		
		DBCursor ret = coll1.find();
		
		List<DBObject> list = new ArrayList<DBObject>();
		int count=1;
		while(ret.hasNext()){
			DBObject obj = ret.next();
			list.add(obj);
			if(count++%1000==0){
				col2.insert(list);
				list = new ArrayList<DBObject>();
			}
		}
		if(list.size()>0)
			col2.insert(list);
	}
	
	private void copyTest()throws Exception{
		Mongo m1 = new Mongo("192.168.1.219");
		DB db1 = m1.getDB("asphaltum_test1");
		DBCollection coll1 = db1.getCollection("asphaltum");
		
		Mongo m2 = new Mongo("192.168.1.229");
		DB db2 = m2.getDB("asphaltum_test1");
		DBCollection col2 = db2.getCollection("asphaltum");
		
		DBCursor ret = coll1.find();
//		DBCursor ret = coll1.find(new BasicDBObject("equipId",
//				new BasicDBObject("$in", new String[] { 
//						"21030000004",
//				        "21060000001",
//				        "21030000002" })));
		System.out.println(ret.count());
		List<DBObject> list = new ArrayList<DBObject>();
		int count=1;
		while(ret.hasNext()){
			DBObject obj = ret.next();
//			obj.put("real_vlaue", getValue(obj));
//			obj.put("real_date", getRealDateStr(obj));
//			obj.put("real_date_long", getRealDateLong(obj));
//			obj.put("real_day", obj.get("real_date").toString().split("\\s")[0]);
			
			
			list.add(obj);
			if(count++%1000==0){
				col2.insert(list);
				list = new ArrayList<DBObject>();
			}
				
			
//			break;
		}
		if(list.size()>0)
			col2.insert(list);
		//===165906===
	}
	
	private void updateTest()throws Exception{
		mongo = new Mongo("192.168.1.219");
		
		DB db = mongo.getDB("asphaltum_test2");
		DBCollection coll = db.getCollection("asphaltum");
		
		DBCursor ret = coll.find();
		while(ret.hasNext()){
			DBObject obj = ret.next();
			DBObject newObj = new BasicDBObject();
			newObj.put("real_day", obj.get("real_date").toString().split("\\s")[0]);
			coll.update(obj, new BasicDBObject("$set", newObj));
//			System.out.println(obj);
//			break;
		}
	}
	
	private void analysisTest()throws Exception{
		mongo = new Mongo("192.168.1.219");
		DB db = mongo.getDB("asphaltum_test1");
		DBCollection coll = db.getCollection("asphaltum");
		
		DBObject cond = new BasicDBObject();
		cond.put("monitorId","21040000002-101");
		cond.put("real_vlaue",new BasicDBObject("$gt",-120).append("$lt", 101));
		System.out.println(cond);
		DBCursor ret = coll.find(cond);
		System.out.println(ret.count());
	}
	
	private String getRealDateStr(DBObject obj)throws Exception{
		String dateStr = obj.get("remark").toString();
		dateStr = dateStr.split("#")[1];
		dateStr = dateStr.split("\\.")[0];
		return dateStr;
	}
	
	private long getRealDateLong(DBObject obj)throws Exception{
		String dateStr = obj.get("remark").toString();
		dateStr = dateStr.split("#")[1];
		dateStr = dateStr.split("\\.")[0];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr).getTime();
	}
	
	
	private double getValue(DBObject obj){
		String ss=obj.get("value").toString();
		ss = ss.substring(0,ss.indexOf("#"));
		return Double.parseDouble(ss);
	}
}
