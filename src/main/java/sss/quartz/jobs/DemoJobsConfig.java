package sss.quartz.jobs;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.quartz.JobBuilder.*;

/**
 * Spring configuration for system jobs. Following a convention of
 * &lt;<i>group</i>&gt;JobsConfig for the spring configuration, containing
 * &lt;<i>jobName</i>&gt;Job beans with the job implementations.
 * 
 * @author jsteele
 */
@ComponentScan
@Configuration
public class DemoJobsConfig {
	@Bean
	@Qualifier("systemJob")
	public JobDetail jobStoreJob() {
		// @formatter:off
		return newJob(JobStoreJob.class)
				.storeDurably()
				.withIdentity("jobstore", "demoJob")
				.withDescription("Demonstration job to illustrate job store.")
				.build();
		// @formatter:on
	}

	@Bean
	@Qualifier("systemJob")
	public JobDetail helloJob() {
		// @formatter:off
		return newJob(HelloWorldJob.class)
				.storeDurably()
				.withIdentity("hello", "demoJob")
				.withDescription("Demonstration job to print hello world.")
				.build();
		// @formatter:on
	}

	@Bean
	@Qualifier("systemJob")
	public JobDetail printMemoryJob() {
		// @formatter:off
		return newJob(PrintMemoryJob.class)
				.storeDurably()
				.withIdentity("printMemory")
				.withDescription("Demonstration job to dump current heap usage.")
				.build();
		// @formatter:off
	}
	
	@Bean
	@Qualifier("systemJob")
	public JobDetail exceptionJob() {
		// @formatter:off
		return newJob(RetryJobWrapper.class)
				.storeDurably()
				.withIdentity("exception", "demoJob")
				.withDescription("An important job that fails with an exception and is retried.")
				.usingJobData(RetryJobWrapper.WRAPPED_JOB_KEY, ExceptionJob.class.getName())
				// Set defaults - can be overridden in trigger definition in schedule file
				.usingJobData(RetryJobWrapper.MAX_RETRIES_KEY, "3")
				.usingJobData(RetryJobWrapper.RETRY_DELAY_KEY, "2")
				.build();
		// @formatter:off
	}
}
