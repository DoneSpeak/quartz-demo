package it.sss.quartz;

import it.sss.quartz.JobStoreSchedulerTest.TestConfig;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sss.jmx.JmxConfig;
import sss.quartz.SchedulerConfig;
import sss.quartz.jobs.DemoJobsConfig;

import com.mchange.v2.c3p0.DriverManagerDataSource;

/**
 * Test the scheduler configuration.
 * 
 * @author jsteele
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SchedulerConfig.class, DemoJobsConfig.class, JmxConfig.class,
		TestConfig.class })
public class JobStoreSchedulerTest {
	@Configuration
	@PropertySource("it/sss/quartz/JobStoreSchedulerTest.properties")
	public static class TestConfig {
		@Bean
		@Qualifier("quartzDataSource")
		public DataSource dataSource() {
			final DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClass("org.h2.Driver");

			final String testDbPath = System.getProperty("quartz.test.db");
			if (null == testDbPath) {
				throw new IllegalArgumentException("quartz.test.db system property is not set");
			}

			dataSource.setJdbcUrl("jdbc:h2:" + testDbPath);
			return dataSource;
		}
	}

	@BeforeClass
	public static void setSysProps() {
		// By default Quartz calls back home to see if there are updates...
		System.setProperty("org.terracotta.quartz.skipUpdateCheck", "true");
		System.setProperty("quartz.test.db", "./test-db/jobstore-test");
	}

	@Test
	@DirtiesContext
	public void sleepAndSoak() throws InterruptedException {
		for (;;) {
			Thread.sleep(100);
		}
	}
}
