package sss.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This job throws an exception to demonstrate error handling and retry logic
 * for {@link ExceptionJob}.
 * 
 * @author jsteele
 */
public class ExceptionJob implements Job {
	/** Class logger. */
	private static final Logger log = LoggerFactory.getLogger(ExceptionJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Exception - executing: " + context);

		throw new JobExecutionException("error occurred in running the job", true);
	}
}
