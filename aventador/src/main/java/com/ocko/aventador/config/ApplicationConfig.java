package com.ocko.aventador.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@MapperScan(basePackages = "${spring.custom.basePackage}.dao.persistence")
@EnableAsync
@ComponentScan(basePackages = "${spring.custom.basePackage}")
@EnableTransactionManagement(mode=AdviceMode.PROXY, proxyTargetClass=true)
public class ApplicationConfig implements WebMvcConfigurer{

	/**
	 * 멀티 스레드 풀 사이즈를 설정하고,
	 * 풀이 소진될 경우 호출자가 실행하도록 설정 
	 * @return
	 */
	@Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.initialize();
        taskExecutor.setRejectedExecutionHandler(new CallerRunsPolicy());
        return taskExecutor;
    }
	
	/**
	 * 정적 리소스 요청 "/static/**"을 resources/static/ 디렉토리로 맵핑
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
	}
	
	/**
	 * URL의 trailing slash가 있는 경우 디렉토리로, 없는 경우 파일로 처리하기 위한 설정 
	 * @param configurer
	 */
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseTrailingSlashMatch(false);
	}
}
