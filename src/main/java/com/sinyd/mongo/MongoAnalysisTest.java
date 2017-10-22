package com.sinyd.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
import com.mongodb.MapReduceCommand.OutputType;

/*
 * 依据测试数据，检测mongo的分析性能
 * */
public class MongoAnalysisTest {

	private long startlong;
	private Mongo mongo;
	private DB db;
	private DBCollection coll;

	public void test01()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("monitorId", "21040000002-001");
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		this.start();
		obj = new BasicDBObject();
		obj.put("monitorId", "21040000002-001");
		obj.put("real_vlaue", 0);
		long numzero = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		System.out.println(num+"##"+numzero);
		System.out.println(numzero*100.0/num);
	}
	
	
	public void test02()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("monitorId", "21040000002-001");
		obj.put("real_day", new BasicDBObject().append("$lt", "2011-07-10").append("$gt", "2011-07-01"));
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		this.start();
		obj = new BasicDBObject();
		obj.put("monitorId", "21040000002-001");
		obj.put("real_vlaue", 0);
		obj.put("real_day", new BasicDBObject().append("$lt", "2011-07-10").append("$gt", "2011-07-01"));
		long numzero = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		System.out.println(num+"##"+numzero);
		System.out.println(numzero*100.0/num);
	}
	
	public void test03()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("equipId", "21040000002");
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		System.out.println(num);
		
		this.start();
		DBObject keyobj = new BasicDBObject("monitorId", true);
		DBObject condobj = new BasicDBObject("equipId","21040000002").append("real_vlaue", 0);
		DBObject initialobj = new BasicDBObject("count", 0);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ ");
		reduce.append("  pre.count++; ");
		reduce.append(" }");
		
		BasicDBList resList = (BasicDBList)coll.group(keyobj, condobj, initialobj, reduce.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		mySort(resList, -1);
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test03MapReduce()throws Exception{
		this.start();
		
		StringBuilder mapsb = new StringBuilder();
		mapsb.append(" function(){							");
		mapsb.append(" 	emit(this.monitorId, 1);		");
		mapsb.append(" }											");
		
		        
		StringBuilder reducesb = new StringBuilder();
		reducesb.append(" function(key, vals){		");
		reducesb.append(" 	var sum = 0;			");
		reducesb.append(" 	for(var i in vals){		");
		reducesb.append(" 		sum+=vals[i];		");
		reducesb.append(" 	}								");
		reducesb.append(" 	return sum;				");
		reducesb.append(" }									");
		
		DBObject query = new BasicDBObject("equipId","21040000002").append("real_vlaue", 0);
		
		MapReduceOutput res = coll.mapReduce(mapsb.toString(), reducesb.toString(), "tmp", query);
		DBCursor cur = res.getOutputCollection().find().sort(new BasicDBObject("value",-1));
		while(cur.hasNext()){
			System.out.println(cur.next());
		}
		
//		MapReduceOutput res = coll.mapReduce(mapsb.toString(), reducesb.toString(), null, OutputType.INLINE, query);
//		for (DBObject obj : res.results()) {
//			System.out.println(obj);
//		}
		
		System.out.println("Elapse Time is "+this.stopAndGet());
	}
	
	
	public void test04()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("equipId", "21040000002");
		obj.put("real_day", new BasicDBObject().append("$lt", "2011-07-10").append("$gt", "2011-07-01"));
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		System.out.println(num);
		
		this.start();
		DBObject keyobj = new BasicDBObject("monitorId", true);
		DBObject condobj = new BasicDBObject("equipId","21040000002").append("real_vlaue", 0);
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-07-10").append("$gt", "2011-07-01"));
		DBObject initialobj = new BasicDBObject("count", 0);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ ");
		reduce.append("  pre.count++; ");
		reduce.append(" }");
		
		BasicDBList resList = (BasicDBList)coll.group(keyobj, condobj, initialobj, reduce.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		mySort(resList, -1);
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test05()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("dept_dir", "001");
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		System.out.println(num);
		
		this.start();
		DBObject keyobj = new BasicDBObject("equipId", true);
		DBObject condobj = new BasicDBObject("dept_dir","001").append("real_vlaue", 0);
		DBObject initialobj = new BasicDBObject("count", 0);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ ");
		reduce.append("  pre.count++; ");
		reduce.append(" }");
		
		BasicDBList resList = (BasicDBList)coll.group(keyobj, condobj, initialobj, reduce.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		mySort(resList, -1);
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test06()throws Exception{
		this.start();
		DBObject obj = new BasicDBObject();
		obj.put("dept_dir", "001");
		obj.put("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		long num = coll.count(obj);
		System.out.println("Elapse Time is "+this.stopAndGet());
		System.out.println(num);
		
		this.start();
		DBObject keyobj = new BasicDBObject("equipId", true);
		DBObject condobj = new BasicDBObject("dept_dir","001").append("real_vlaue", 0);
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		DBObject initialobj = new BasicDBObject("count", 0);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ ");
		reduce.append("  pre.count++; ");
		reduce.append(" }");
		
		BasicDBList resList = (BasicDBList)coll.group(keyobj, condobj, initialobj, reduce.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		
		mySort(resList, -1);
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test06MapReduce()throws Exception{
		this.start();
		StringBuilder mapsb = new StringBuilder();
		mapsb.append(" function(){							");
		mapsb.append(" 	emit(this.equipId, 1);		");
		mapsb.append(" }											");
		
		        
		StringBuilder reducesb = new StringBuilder();
		reducesb.append(" function(key, vals){		");
		reducesb.append(" 	var sum = 0;			");
		reducesb.append(" 	for(var i in vals){		");
		reducesb.append(" 		sum+=vals[i];		");
		reducesb.append(" 	}								");
		reducesb.append(" 	return sum;				");
		reducesb.append(" }									");
		
		DBObject query = new BasicDBObject("dept_dir","001").append("real_vlaue", 0).append("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		
		MapReduceCommand command = new MapReduceCommand(coll, mapsb.toString(), reducesb.toString(), "tmp", OutputType.REPLACE, query);
		MapReduceOutput res = coll.mapReduce(command);
//		MapReduceOutput res = coll.mapReduce(mapsb.toString(), reducesb.toString(), "tmp", query);
		DBCursor cur = res.getOutputCollection().find().sort(new BasicDBObject("value",-1));
		while(cur.hasNext()){
			System.out.println(cur.next());
		}
		db.getCollection("tmp").drop();
		
		System.out.println("Elapse Time is "+this.stopAndGet());
	}
	
	public void test07()throws Exception{
		this.start();
		DBObject keyobj = new BasicDBObject("monitorId", true);
//		DBObject condobj = new BasicDBObject("monitorId", "21040000002-006").append("real_vlaue", new BasicDBObject("$ne", 0));
		DBObject condobj = new BasicDBObject("equipId", "21040000002").append("real_vlaue", new BasicDBObject("$ne", 0));
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		DBObject initialobj = new BasicDBObject("count", 0).append("min", Integer.MAX_VALUE).append("max", Integer.MIN_VALUE).append("avg", 0).append("total", 0);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ 						");
		reduce.append("  	pre.count++; 							");
		reduce.append("  	pre.total+=cur.real_vlaue; 		");
		reduce.append(" 	if(cur.real_vlaue<pre.min){ 		");
		reduce.append("  		pre.min=cur.real_vlaue; 		");
		reduce.append("  	}												");
		reduce.append("  	if(cur.real_vlaue>pre.max){ 		");
		reduce.append("  		pre.max=cur.real_vlaue; 		");
		reduce.append("  	} 												");
		reduce.append(" }													");
		
		StringBuffer finalize = new StringBuffer();
		finalize.append(" function(pre){ 							");
		finalize.append(" 	pre.avg=0;		 						");
		finalize.append(" 	if(pre.count>0){ 						");
		finalize.append("  		pre.avg=pre.total/pre.count; ");
		finalize.append("  	}												");
		finalize.append(" }													");
		
		DBObject res = coll.group(keyobj, condobj, initialobj, reduce.toString(), finalize.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
//		System.out.println(res);
		
		BasicDBList resList = (BasicDBList)res;
		Collections.sort(resList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((BasicDBObject)o1).getString("monitorId").compareTo(((BasicDBObject)o2).getString("monitorId"));
			}
			
		});
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test07MapReduce()throws Exception{
		this.start();
		StringBuilder mapsb = new StringBuilder();
		mapsb.append(" function(){							");
		mapsb.append(" 	emit(this.monitorId, {'count':1, 'min':this.real_vlaue, 'max':this.real_vlaue,  'avg':0, 'total':this.real_vlaue});		");
		mapsb.append(" }											");
		
		        
		StringBuilder reducesb = new StringBuilder();
		reducesb.append(" function(key, vals){						");
		reducesb.append(" 	var res = {'count':0, 'min':1000, 'max':-1000, 'avg':0, 'total':0};			");
		reducesb.append(" 	for(var i in vals){						");
		reducesb.append(" 		res.total+=vals[i].total;			");
		reducesb.append(" 		res.count+=vals[i].count;		");
		reducesb.append(" 		if(res.max<vals[i].max) 	res.max=vals[i].max;	");
		reducesb.append(" 		if(res.min>vals[i].min)	    res.min=vals[i].min; 		");
		reducesb.append(" 	}								");
		reducesb.append(" 	return res;				");
		reducesb.append(" }									");
		
		StringBuilder finalizesb = new StringBuilder();
		finalizesb.append(" function(key, val){ 	 					");
		finalizesb.append(" 	val.avg=0;		 						");
		finalizesb.append(" 	if(val.count>0){ 						");
		finalizesb.append("  		val.avg=val.total/val.count;	");
		finalizesb.append("  	}												");
		finalizesb.append("  	return val;									");
		finalizesb.append(" }													");
		
		DBObject query = new BasicDBObject("equipId","21040000002").append("real_vlaue", new BasicDBObject("$ne", 0)).append("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		
		MapReduceCommand command = new MapReduceCommand(coll, mapsb.toString(), reducesb.toString(), "tmp", OutputType.REPLACE, query);
		command.setFinalize(finalizesb.toString());
		MapReduceOutput res = coll.mapReduce(command);
		
		DBCursor cur = res.getOutputCollection().find().sort(new BasicDBObject("_id",1));
		while(cur.hasNext()){
			System.out.println(cur.next());
		}
		db.getCollection("tmp").drop();
		
		System.out.println("Elapse Time is "+this.stopAndGet());
	}
	
	public void test08()throws Exception{
		this.start();
		DBObject keyobj = new BasicDBObject("monitorId", true);
		DBObject condobj = new BasicDBObject("monitorId", "21040000002-001");
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		DBObject initialobj = new BasicDBObject("count", 0).append("total", 0).append("rate", 0).append("standart", 420).append("threshold", 0.1);
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ 						");
		reduce.append("  	pre.total++; 								");
		reduce.append(" 	if(cur.real_vlaue<pre.standart*(1-pre.threshold) || cur.real_vlaue>pre.standart*(1+pre.threshold)){ 		");
		reduce.append("  		pre.count++; 						");
		reduce.append("  	}												");
		reduce.append(" }													");
		
		StringBuffer finalize = new StringBuffer();
		finalize.append(" function(pre){ 							");
		finalize.append(" 	if(pre.total>0){ 						");
		finalize.append("  		pre.rate=pre.count/pre.total; ");
		finalize.append("  	}												");
		finalize.append(" }													");
		
		DBObject res = coll.group(keyobj, condobj, initialobj, reduce.toString(), finalize.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		System.out.println(res);
	}
	
	public void test09()throws Exception{
		this.start();
		DBObject keyobj = new BasicDBObject("monitorId", true);
		DBObject condobj = new BasicDBObject("equipId", "21040000002");
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		
		
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21040000002-001").append("standart", 420));
		list.add(new BasicDBObject("monitorId","21040000002-002").append("standart", 1080));
		list.add(new BasicDBObject("monitorId","21040000002-003").append("standart", 1210));
		list.add(new BasicDBObject("monitorId","21040000002-004").append("standart", 360));
		list.add(new BasicDBObject("monitorId","21040000002-005").append("standart", 730));
		list.add(new BasicDBObject("monitorId","21040000002-006").append("standart", 100));
		list.add(new BasicDBObject("monitorId","21040000002-007").append("standart", 156));
		list.add(new BasicDBObject("monitorId","21040000002-008").append("standart", 1));
		list.add(new BasicDBObject("monitorId","21040000002-009").append("standart", 70));
		list.add(new BasicDBObject("monitorId","21040000002-010").append("standart", 75));
		list.add(new BasicDBObject("monitorId","21040000002-101").append("standart", -100));
		BasicDBObject initialobj = new BasicDBObject("threshold", 0.1).append("standart", 0).append("count", 0).append("zeroCount", 0).append("total", 0).append("rate", 0);
		initialobj.append("monitorArray", list);
		
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ 												");
		reduce.append("  	var monObj, little, big;										");
		reduce.append("  	for(i=0;i<pre.monitorArray.length;i++) {			");
		reduce.append("  		monObj=pre.monitorArray[i];						");
		reduce.append("  		if(cur.monitorId===monObj.monitorId){		");
		reduce.append("  			pre.standart=monObj.standart;					");
		reduce.append("  			if(cur.real_vlaue===0) 	pre.zeroCount++;	");
		reduce.append("  			pre.total++;												");
		reduce.append("  			little=monObj.standart>0 ? monObj.standart*(1-pre.threshold) : monObj.standart*(1+pre.threshold);	");
		reduce.append("  			big=monObj.standart>0 ? monObj.standart*(1+pre.threshold) : monObj.standart*(1-pre.threshold);	");
		reduce.append("  			if(cur.real_vlaue<little || cur.real_vlaue>big){				");
		reduce.append("  				pre.count++;										");
		reduce.append("  			}																");
		reduce.append("  			break;														");
		reduce.append("  		}																	");
		reduce.append("  	}																		");
		reduce.append(" }																			");

		
		
		StringBuffer finalize = new StringBuffer();
		finalize.append(" function(prev){ 														");
		finalize.append(" 		delete prev.monitorArray;									");
		finalize.append(" 		if(prev.total>0){ 												");
		finalize.append("  			prev.rate=prev.count/prev.total; 					");
		finalize.append("  		}																		");
		finalize.append(" }																				");
		
		DBObject res = coll.group(keyobj, condobj, initialobj, reduce.toString(), finalize.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		BasicDBList resList = (BasicDBList)res;
		Collections.sort(resList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((BasicDBObject)o1).getString("monitorId").compareTo(((BasicDBObject)o2).getString("monitorId"));
			}
			
		});
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	public void test09MapReduce()throws Exception{
		this.start();
		
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21040000002-001").append("standart", 420));
		list.add(new BasicDBObject("monitorId","21040000002-002").append("standart", 1080));
		list.add(new BasicDBObject("monitorId","21040000002-003").append("standart", 1210));
		list.add(new BasicDBObject("monitorId","21040000002-004").append("standart", 360));
		list.add(new BasicDBObject("monitorId","21040000002-005").append("standart", 730));
		list.add(new BasicDBObject("monitorId","21040000002-006").append("standart", 100));
		list.add(new BasicDBObject("monitorId","21040000002-007").append("standart", 156));
		list.add(new BasicDBObject("monitorId","21040000002-008").append("standart", 1));
		list.add(new BasicDBObject("monitorId","21040000002-009").append("standart", 70));
		list.add(new BasicDBObject("monitorId","21040000002-010").append("standart", 75));
		list.add(new BasicDBObject("monitorId","21040000002-101").append("standart", -100));
		
		Map<String, Object> scopeMap = new HashMap<String, Object>();
		scopeMap.put("threshold", 0.1);
		scopeMap.put("stdlist", list);
		
		StringBuilder mapsb = new StringBuilder();
		mapsb.append(" function(){							");
		mapsb.append(" 	emit(this.monitorId, {'count':1, 'total':1, 'rate':0, 'value':this.real_vlaue, 'flag':0});		");
		mapsb.append(" }											");
		
		        
		StringBuilder reducesb = new StringBuilder();
		reducesb.append(" function(key, vals){																	");
		reducesb.append(" 	var res = {'count':0, 'total':0, 'rate':0, 'value':0,  'flag':1};	");
		reducesb.append(" 	var little, big;																			");
		reducesb.append(" 	for(var k in stdlist){																");
		reducesb.append(" 		if(key===stdlist[k].monitorId){											");
		reducesb.append(" 			little=stdlist[k].standart>0 ? stdlist[k].standart*(1-threshold) : stdlist[k].standart*(1+threshold);						");
		reducesb.append(" 			big=stdlist[k].standart>0 ? stdlist[k].standart*(1+threshold) : stdlist[k].standart*(1-threshold);						");
		reducesb.append(" 			for(var i in vals){															");
		reducesb.append(" 				res.total+=vals[i].total;												");
		reducesb.append(" 				if(vals[i].flag===1 || vals[i].value>big || vals[i].value<little){	     			");
		reducesb.append(" 					res.count+=vals[i].count; 										");
		reducesb.append(" 				} 																				");
		reducesb.append(" 			}																					");
		reducesb.append(" 			break;																			");
		reducesb.append(" 		}																						");
		reducesb.append(" 	}																							");
		reducesb.append(" 	return res;																			");
		reducesb.append(" }																								");
		
		StringBuilder finalizesb = new StringBuilder();
		finalizesb.append(" function(key, val){ 	 					");
		finalizesb.append(" 	val.rate=0;		 						");
		finalizesb.append(" 	if(val.total>0){ 							");
		finalizesb.append("  		val.rate=val.count/val.total;	");
		finalizesb.append("  	}												");
		finalizesb.append("  	delete val.value;						");
		finalizesb.append("  	delete val.flag;						");
		finalizesb.append("  	return val;									");
		finalizesb.append(" }													");
		
		DBObject query = new BasicDBObject("equipId","21040000002").append("real_day", new BasicDBObject().append("$lt", "2011-09-10").append("$gt", "2011-05-01"));
		
		
		MapReduceCommand command = new MapReduceCommand(coll, mapsb.toString(), reducesb.toString(), "tmp", OutputType.REPLACE, query);
		command.setFinalize(finalizesb.toString());
		command.setScope(scopeMap);
		MapReduceOutput res = coll.mapReduce(command);
		
		DBCursor cur = res.getOutputCollection().find().sort(new BasicDBObject("_id",1));
		while(cur.hasNext()){
			System.out.println(cur.next());
		}
		db.getCollection("tmp").drop();
		
		System.out.println("Elapse Time is "+this.stopAndGet());
		
	}
	
	public void test10()throws Exception{
		this.start();
		DBObject keyobj = new BasicDBObject("equipId", true);
		DBObject condobj = new BasicDBObject("dept_dir", "003");
		condobj.put("real_day", new BasicDBObject().append("$lt", "2011-12-31").append("$gt", "2011-01-01"));
		
		
		List<List<BasicDBObject>> devlist = new ArrayList<List<BasicDBObject>>();
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21030000001-001").append("standart", 750));
		list.add(new BasicDBObject("monitorId","21030000001-002").append("standart", 451));
		list.add(new BasicDBObject("monitorId","21030000001-003").append("standart", 838));
		list.add(new BasicDBObject("monitorId","21030000001-004").append("standart", 877));
		list.add(new BasicDBObject("monitorId","21030000001-005").append("standart", 594));
		list.add(new BasicDBObject("monitorId","21030000001-006").append("standart", 425));
		list.add(new BasicDBObject("monitorId","21030000001-007").append("standart", 192));
		list.add(new BasicDBObject("monitorId","21030000001-008").append("standart", 0.3));
		list.add(new BasicDBObject("monitorId","21030000001-009").append("standart", 81));
		list.add(new BasicDBObject("monitorId","21030000001-010").append("standart", 89));
		list.add(new BasicDBObject("monitorId","21030000001-101").append("standart", 1));
		devlist.add(list);
		
		list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21040000002-001").append("standart", 420));
		list.add(new BasicDBObject("monitorId","21040000002-002").append("standart", 1080));
		list.add(new BasicDBObject("monitorId","21040000002-003").append("standart", 1210));
		list.add(new BasicDBObject("monitorId","21040000002-004").append("standart", 360));
		list.add(new BasicDBObject("monitorId","21040000002-005").append("standart", 730));
		list.add(new BasicDBObject("monitorId","21040000002-006").append("standart", 100));
		list.add(new BasicDBObject("monitorId","21040000002-007").append("standart", 156));
		list.add(new BasicDBObject("monitorId","21040000002-008").append("standart", 1));
		list.add(new BasicDBObject("monitorId","21040000002-009").append("standart", 70));
		list.add(new BasicDBObject("monitorId","21040000002-010").append("standart", 75));
		list.add(new BasicDBObject("monitorId","21040000002-101").append("standart", -100));
		devlist.add(list);
		
		list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21050000003-001").append("standart", 660));
		list.add(new BasicDBObject("monitorId","21050000003-002").append("standart", 577));
		list.add(new BasicDBObject("monitorId","21050000003-003").append("standart", 421));
		list.add(new BasicDBObject("monitorId","21050000003-004").append("standart", 182));
		list.add(new BasicDBObject("monitorId","21050000003-005").append("standart", 378));
		list.add(new BasicDBObject("monitorId","21050000003-006").append("standart", 614));
		list.add(new BasicDBObject("monitorId","21050000003-007").append("standart", 120));
		list.add(new BasicDBObject("monitorId","21050000003-008").append("standart", 1));
		list.add(new BasicDBObject("monitorId","21050000003-009").append("standart", 112));
		list.add(new BasicDBObject("monitorId","21050000003-010").append("standart", 130));
		list.add(new BasicDBObject("monitorId","21050000003-101").append("standart", 133));		
		devlist.add(list);
		
		list = new ArrayList<BasicDBObject>();
		list.add(new BasicDBObject("monitorId","21090000002-001").append("standart", 381));
		list.add(new BasicDBObject("monitorId","21090000002-002").append("standart", 634));
		list.add(new BasicDBObject("monitorId","21090000002-003").append("standart", 910));
		list.add(new BasicDBObject("monitorId","21090000002-004").append("standart", 534));
		list.add(new BasicDBObject("monitorId","21090000002-005").append("standart", 628));
		list.add(new BasicDBObject("monitorId","21090000002-006").append("standart", 1112));
		list.add(new BasicDBObject("monitorId","21090000002-007").append("standart", 140));
		list.add(new BasicDBObject("monitorId","21090000002-008").append("standart", 1));
		list.add(new BasicDBObject("monitorId","21090000002-009").append("standart", 133));
		list.add(new BasicDBObject("monitorId","21090000002-010").append("standart", 72));
		list.add(new BasicDBObject("monitorId","21090000002-101").append("standart", 1));				
		devlist.add(list);
		
		
		BasicDBObject initialobj = new BasicDBObject("threshold", 0.1).append("count", 0).append("zeroCount", 0).append("total", 0).append("rate", 0);
		initialobj.append("monitorArray", devlist);
		
		StringBuffer reduce = new StringBuffer();
		reduce.append(" function(cur, pre){ 													");
		reduce.append("  	var monList, monObj, little, big, flag=false;				");
		reduce.append("  	for(i=0;i<pre.monitorArray.length;i++) {				");
		reduce.append("  		flag=false;															");
		reduce.append("  		monList=pre.monitorArray[i];							");
		reduce.append("  		for(k=0;k<monList.length;k++) {						");
		reduce.append("  			monObj=monList[k];						");
		reduce.append("  			if(cur.monitorId===monObj.monitorId){		");
		reduce.append("  				if(cur.real_vlaue===0) 	pre.zeroCount++;	");
		reduce.append("  				pre.total++;												");
		reduce.append("  				little=monObj.standart>0 ? monObj.standart*(1-pre.threshold) : monObj.standart*(1+pre.threshold);	");
		reduce.append("  				big=monObj.standart>0 ? monObj.standart*(1+pre.threshold) : monObj.standart*(1-pre.threshold);	");
		reduce.append("  				if(cur.real_vlaue<little || cur.real_vlaue>big){				");
		reduce.append("  					pre.count++;										");
		reduce.append("  				}																");
		reduce.append("  				flag=true;													");
		reduce.append("  				break;														");
		reduce.append("  			}																	");
		reduce.append("  		}																		");
		reduce.append("  		if(flag) break;													");
		reduce.append("  	}																			");
		reduce.append(" }																				");

		
		
		StringBuffer finalize = new StringBuffer();
		finalize.append(" function(prev){ 														");
		finalize.append(" 		delete prev.monitorArray;									");
		finalize.append(" 		if(prev.total>0){ 												");
		finalize.append("  			prev.rate=prev.count/prev.total; 					");
		finalize.append("  		}																		");
		finalize.append(" }																				");
		
		DBObject res = coll.group(keyobj, condobj, initialobj, reduce.toString(), finalize.toString());
		System.out.println("Elapse Time is "+this.stopAndGet());
		
		BasicDBList resList = (BasicDBList)res;
		Collections.sort(resList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((BasicDBObject)o1).getString("equipId").compareTo(((BasicDBObject)o2).getString("equipId"));
			}
			
		});
		for (Object object : resList) {
			BasicDBObject tmp = (BasicDBObject)object;
			System.out.println(tmp);
		}
	}
	
	
	private void mySort(BasicDBList srcList, int flag){
		Collections.sort(srcList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				int res = ((BasicDBObject)o1).getInt("count")-((BasicDBObject)o2).getInt("count");
				return res;
			}
		});
		
		if(flag==-1){
			Collections.reverse(srcList);
		}
	}
	
	private void start()throws Exception {
		this.mongo= new Mongo("192.168.1.229");
		this.db = mongo.getDB("allen_test");
		this.coll = db.getCollection("test");
		this.startlong=System.currentTimeMillis();
	}
	
	private int stopAndGet()throws Exception {
		long t = System.currentTimeMillis()-this.startlong;
		return (int)t;
	}
}
