package quartz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobTwo implements Job {
	private static Logger log = LoggerFactory.getLogger(JobTwo.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("========TWO START=========");
		try {

			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.createStatement().executeUpdate("insert into test_td (content) values ('" + new Date() + "');");
			Thread.sleep(20*1000);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
		log.info("========TWO END=========");
	}

}
