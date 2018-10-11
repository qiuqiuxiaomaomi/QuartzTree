package com.bonaparte.service;

import com.bonaparte.constant.Constant;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public class MyQuartzJobBean extends QuartzJobBean {
    private SimpleService simpleService;

    public ApplicationContext applicationContext = null;

    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) {
        //获取taskId
        String taskId = (String)jobexecutioncontext.getMergedJobDataMap().get(Constant.JOB_PARAM_KEY);
        System.out.println("开始执行任务,taskId="+taskId);
        try{
            applicationContext = getAppliCxt(jobexecutioncontext);
            simpleService = (SimpleService)applicationContext.getBean("simpleService");
            //执行正在的业务逻辑方法.
            simpleService.jobMethod(taskId);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("执行任务"+jobexecutioncontext.getTrigger().getKey().getName()+"出错!");
        }
    }

    public ApplicationContext getAppliCxt(JobExecutionContext context){
        try {
            return (ApplicationContext)context.getScheduler().getContext().get("applicationContextKey");
        } catch (SchedulerException e) {
            System.out.println("获取spring的applicationContext失败!原因为"+e.getMessage());
        }
        return null;
    }
}
