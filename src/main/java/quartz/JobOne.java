package quartz;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobOne implements Job {

	private static Logger log = LoggerFactory.getLogger(JobOne.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("========ONE START=========");
		FileWriter write=null;
		try {
			write = new FileWriter("/Users/allen/temp/jobone.log", true);
			String nowStr = "====="+new Date()+"=====";
			
			write.write(nowStr);
			write.write(System.getProperty("line.separator"));
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("========ONE END=========");

	}

}
