package sss.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scheduled job to print hello world.
 * 
 * @author jsteele
 */
public class HelloWorldJob implements Job {
	/** Class logger. */
	private static final Logger log = LoggerFactory.getLogger(HelloWorldJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Hello World - executing: " + context);
	}
}
