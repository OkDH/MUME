/**
 * 
 */
package com.ocko.aventador.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author ok
 * 스프링 스케쥴러는 기본적으로 한개의 쓰레드 풀에서 동작.
 * 풀을 더 생성하여 스케줄 작업을 할 수 있도록 설정
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		threadPoolTaskScheduler.setThreadNamePrefix("my-scheduler-task-pool-");
		threadPoolTaskScheduler.initialize();
		
		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
	
}
