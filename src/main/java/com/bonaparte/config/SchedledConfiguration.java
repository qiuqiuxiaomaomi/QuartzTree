package com.bonaparte.config;

import org.quartz.impl.StdScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by yangmingquan on 2018/10/11.
 */
@Configuration
public class SchedledConfiguration {
    @Resource
    private DataSource datasource;
    @Resource
    private StdScheduler quartzScheduler;

    @Bean(name="quartzScheduler")
    public SchedulerFactoryBean quartzScheduler(){
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
        quartzScheduler.setDataSource(datasource);
        quartzScheduler.setApplicationContextSchedulerContextKey("applicationContextKey");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        quartzScheduler.setConfigLocation(resolver.getResource("classpath:quartz.properties"));
        return quartzScheduler;
    }
}
