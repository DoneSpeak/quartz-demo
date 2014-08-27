package it.sss.quartz;

import it.sss.quartz.BasicSchedulerTest.TestConfig;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sss.jmx.JmxConfig;
import sss.quartz.SchedulerConfig;
import sss.quartz.jobs.DemoJobsConfig;

/**
 * Test the scheduler configuration.
 * 
 * @author jsteele
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SchedulerConfig.class, DemoJobsConfig.class, JmxConfig.class,
		TestConfig.class })
public class BasicSchedulerTest {
	@Configuration
	@PropertySource("it/sss/quartz/BasicSchedulerTest.properties")
	public static class TestConfig {
	}

	@BeforeClass
	public static void setSysProps() {
		// By default Quartz calls back home to see if there are updates...
		System.setProperty("org.terracotta.quartz.skipUpdateCheck", "true");
	}

	@Test
	@DirtiesContext
	public void sleepAndSoak() throws InterruptedException {
		for (;;) {
			Thread.sleep(100);
		}
	}
}
