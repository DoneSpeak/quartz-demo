package sss.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scheduled job to print current heap utilization.
 * 
 * @author jsteele
 */
public class PrintMemoryJob implements Job {
	/** Class logger. */
	private static final Logger log = LoggerFactory.getLogger(PrintMemoryJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final double total = Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0);
		final double free = Runtime.getRuntime().freeMemory() / (1024.0 * 1024.0);
		final double max = Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0);

		log.info(String.format("current heap: %.1fM/%.1fM of %.1fM used", total - free, total, max));
	}
}
