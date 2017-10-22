package com.sinyd.mongo;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoToFile {

    /**
     * Function: main
     * <P>
     * Description:
     * <P>
     * Others:
     * 
     * @author: Allen
     * @CreateTime: 2012-3-15 ����09:20:13
     * @param args
     *            void
     */
    private String outPath = "E:/data/";
    private DBCollection coll;
    private List<String> list = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        new MongoToFile().writeToFile();
    }

    public MongoToFile() throws Exception {
        Mongo mongo = new Mongo("192.168.1.219");
        DB db = mongo.getDB("asphaltum_data");
        coll = db.getCollection("asphaltum");
 
        list.add("21010000A05");
        list.add("21010000A06");
        list.add("2101000AC03");
        list.add("21020000001");
        list.add("21020000002");
        
        list.add("21030000001");
        list.add("21030000004");
        list.add("21060000001");
        list.add("21030000002");
        list.add("21030000003");
        
        list.add("21040000001");
        list.add("21040000002");
        list.add("21040000004");
        list.add("21050000001");
        list.add("21050000003");
        
        list.add("21060000003");
        list.add("21090000001");
        list.add("21090000002");
        list.add("21110000001");
        list.add("21110000002");
    }

    public void writeToFile() throws Exception {
        String outFile = this.outPath;
        String[] dayArray = { "20110910", "20111010" };
        String[] deptArray = { "001", "002", "003" };

        for (String dayStr : dayArray) {
            for (String deptStr : deptArray) {
                for (String equipStr : this.list) {
                    DBObject cond = new BasicDBObject();
                    cond.put("equipId", equipStr);
                    cond.put("day_dir", dayStr);
                    cond.put("dept_dir", deptStr);

                    DBCursor ret = coll.find(cond);

                    if(ret.count()==0)
                        continue;
                    
                    outFile = this.outPath + dayStr + "/" + deptStr + "/" + equipStr + ".csv";
                    PrintWriter pout = new PrintWriter(new FileOutputStream(outFile), true);
                    System.out.println("==START=="+outFile+"==");
                    while (ret.hasNext()) {
                        DBObject obj = ret.next();
                        pout.print(obj.get("monitorId") + "," + obj.get("value") + "," + obj.get("key_name"));
                        pout.println();
                    }

                    pout.close();
                    System.out.println("##FIN##"+outFile+"##");
                }

            }
        }

    }
}
