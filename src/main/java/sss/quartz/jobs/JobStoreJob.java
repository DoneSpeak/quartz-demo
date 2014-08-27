package sss.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This job executes and logs some information that might be used in recovery
 * from misfires. Quartz does some stuff for us in terms of determining if we
 * need to refire a missed trigger or not, but we might elect to process a
 * little differently on a re-attempt.
 * 
 * @author jsteele
 */
public class JobStoreJob implements Job {
	/** Class logger. */
	private static final Logger log = LoggerFactory.getLogger(JobStoreJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss,SSS");

		if (context.isRecovering()) {
			log.info("\n\t" + context.getTrigger().getKey() + ": recovering [refireCount="
					+ context.getRefireCount() + "]");
			log.debug(context.toString());
		} else {
			log.info("\n\t" + context.getTrigger().getKey() + ": executing [scheduled at "
					+ fmt.format(context.getScheduledFireTime()) + ", delayed "
					+ formatAgeMsg(context.getScheduledFireTime()) + "]");
			log.debug(context.toString());
		}
	}

	/**
	 * Formats a user-printable description of how long ago a time was.
	 * 
	 * @param time
	 *          time to compare
	 * @return description
	 */
	private String formatAgeMsg(Date time) {
		final Date now = new Date();
		final long diffMs = now.getTime() - time.getTime();

		final long days = diffMs / 86400000;
		final long hours = (diffMs % 86400000) / 3600000;
		final long minutes = (diffMs % 3600000) / 60000;
		final long seconds = (diffMs % 60000) / 1000;
		final long ms = (diffMs % 1000);

		final StringBuilder sb = new StringBuilder();

		if (diffMs == 0) {
			sb.append("0.");
		} else {
			if (days > 0) {
				sb.append(days).append("d");
			}
			if (hours > 0) {
				sb.append(hours).append("h");
			}
			if (minutes > 0) {
				sb.append(minutes).append("m");
			}
			sb.append(seconds).append(".");
		}
		sb.append(String.format("%03ds", ms));

		return sb.toString();
	}
}
