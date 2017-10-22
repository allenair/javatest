package quartz;

import java.sql.Connection;
import java.sql.DriverManager;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class FirstQuartzTest {

	private String firstCronSrc = "0 0/1 * * * ?";
	private String secondCronSrc = "0/15 * * * * ?";

	public static void main(String[] args) throws Exception {
		new FirstQuartzTest().start();

	}

	private void start() throws Exception {
		this.initDb();

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		JobDetail job1 = JobBuilder.newJob(JobOne.class)
				.withIdentity("job1", "group1").build();

		CronTrigger trigger1 = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule(firstCronSrc))
				.build();
		
		JobDetail job2 = JobBuilder.newJob(JobTwo.class)
				.withIdentity("job2", "group2").build();

		CronTrigger trigger2 = TriggerBuilder.newTrigger()
				.withIdentity("trigger2", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule(secondCronSrc))
				.build();

		sched.scheduleJob(job1, trigger1);
		sched.scheduleJob(job2, trigger2);
		
		sched.start();
	}

	private void initDb() throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
		c.createStatement().executeUpdate(
				"CREATE TABLE IF NOT EXISTS test_td (id integer primary key AutoIncrement,content TEXT);");
		c.close();
	}
}
