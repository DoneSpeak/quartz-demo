package sss.quartz;

import static sss.quartz.ScheduleItemListBuilder.loadSchedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Spring configuration for the Quartz scheduler.
 * 
 * @author jsteele
 */
@Configuration
@ComponentScan
public class SchedulerConfig {
	/** Class logger. */
	private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

	@Autowired
	private Environment env;

	@Autowired
	@Qualifier("systemJob")
	private Collection<JobDetail> jobs;

	@Autowired(required = false)
	@Qualifier("quartzDataSource")
	private DataSource dataSource;

	@Bean
	public SchedulerFactoryBean schedulerFactory() throws Exception {
		final SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		// TODO - correctly factor out quartz properties
		final Properties quartzProps = new Properties();
		quartzProps.put("org.quartz.scheduler.jmx.export", "true");
		quartzProps.put("org.quartz.scheduler.jmx.objectName", "org.quartz:name=SchedulerMBean");
		quartzProps.put("org.quartz.threadPool.threadCount", "10");

		// For test purposes, support persistent storage only if a data source has
		// been wired up
		if (null != dataSource) {
			schedulerFactory.setDataSource(dataSource);

			quartzProps.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
			quartzProps.put("org.quartz.jobStore.driverDelegateClass",
					"org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
			quartzProps.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
			quartzProps.put("org.quartz.jobStore.useProperties", "true");

		}
		schedulerFactory.setQuartzProperties(quartzProps);
		return schedulerFactory;
	}

	@Bean
	public Scheduler scheduler() throws Exception {
		final Scheduler scheduler = schedulerFactory().getObject();
		/*
		 * Register system jobs. These are the actions we want to be able to refer
		 * to and remember whether there's an active trigger or not. A use case for
		 * this might be that we suspend or resume batch processing, manually invoke
		 * system jobs, etc.
		 * 
		 * These jobs may or may not have associated triggers.
		 * 
		 * For purposes of this demo, we always replace any existing job. We might
		 * choose to let the persistant job store take precedence (as we do below
		 * for triggers.)
		 */
		for (final JobDetail job : jobs) {
			scheduler.addJob(job, true);
		}

		/*
		 * Register the system schedule with the scheduler. These are all the
		 * scheduled tasks that will be kicked off automatically at program startup.
		 * 
		 * There are a couple of approaches that could be taken here, depending on
		 * the requirements.
		 * 
		 * 1) The job store may take precedence no matter what. This would eliminate
		 * the ability to update the trigger definitions through only code
		 * deployment.
		 * 
		 * 2) We might clear the schedule out completely on each restart and have
		 * the code regenerate the triggers. This would blow away any record of
		 * misfired triggers for recovery.
		 * 
		 * 3) We might merge code-defined triggers with whatever is currently in the
		 * database. This adds a more complicated management/upgrade model.
		 * 
		 * For purposes of this demo, we're using option #1. If you want to make a
		 * change to a trigger config you will need to dump the database and
		 * recreate.
		 */
		for (final Trigger trigger : systemSchedule()) {
			if (!scheduler.checkExists(trigger.getKey())) {
				scheduler.scheduleJob(trigger);
			}
		}

		return scheduler;
	}

	@Bean
	public List<Trigger> systemSchedule() throws IOException {
		final String scheduleFile = env.getProperty("sss.schedule.system");
		if (null == scheduleFile) {
			log.warn("no system schedule file configured (sss.schedule.system)");
			return new ArrayList<Trigger>();
		} else {
			return loadSchedule(scheduleFile).build();
		}
	}
}
