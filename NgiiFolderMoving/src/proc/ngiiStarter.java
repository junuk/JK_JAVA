/**
 * 
 */
package proc;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @FileName  : ngiiProc.java
 * @Project     : NgiiFolderMoving
 * @Date         : 2015. 10. 25. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 :
 */
public class ngiiStarter {
	
	// 스케줄러 정보
	private SchedulerFactory schedFact;
	private Scheduler sched;
	
	// 프로퍼티파일 정보
	private Properties pro = null;
	private FileInputStream fis = null;
	
	// 로그정보
	private Logger logger = null;
	
	public ngiiStarter() throws IOException
	{
		this.logger = Logger.getLogger(getClass());
		this.pro = new Properties();
		this.fis = new FileInputStream("property/ngii.properties");
		this.pro.load(new InputStreamReader(fis));
	}
	
	public void cronTriggerStart()
	{
		logger.info("***** 스케줄링 설정 대기 합니다.");
		/*
		 * [01] 스케줄 잡 생성 및 주기 호출 대기
		 */
		try {
			schedFact = new StdSchedulerFactory();
			sched = schedFact.getScheduler();
			sched.start();
			
			JobDetail job1 = newJob(ngiiProc.class).withIdentity("job1",
					"group1").build();
			
			CronTrigger trigger1 = (CronTrigger) newTrigger()
					.withIdentity("trigger1", "group1")
					.withSchedule(cronSchedule(this.pro.getProperty("ngii.folder.schedule.time")))
					.forJob("job1", "group1").build();
			
			// 스케줄 실행
			sched.scheduleJob(job1, trigger1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Method Name  : main
	 * @작성일   : 2015. 10. 25. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ngiiStarter starter = new ngiiStarter();
		starter.cronTriggerStart();
	}

}
