package com.bonaparte.service;

import com.bonaparte.constant.Constant;
import org.quartz.*;
import org.quartz.impl.StdScheduler;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public class SchedulerService {
    @Resource(name = "quartzScheduler")
    private StdScheduler scheduler;

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param cronExpression
     *            Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     * @throws Exception
     */
    public void schedule(String cronExpression) throws Exception {
        schedule(null,null, cronExpression);
    }

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param name
     *            Quartz CronTrigger名称
     * @param cronExpression
     *            Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     * @throws ParseException
     * @throws SchedulerException
     */
    public void schedule(String taskId, String name,String cronExpression) throws RuntimeException {
        try{
            schedule(taskId, name, new CronExpression(cronExpression));
        }catch(Exception e){
            throw new RuntimeException("操作失败");
        }

    }

    public void addTrigger(String name,String cronExpression,String taskId) throws Exception{
        try{
            this.addschedule(new CronExpression(cronExpression), name,taskId);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("新增任务失败");
        }

    }
    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param cronExpression
     *            Quartz CronExpression
     * @throws SchedulerException
     */
    public void schedule(CronExpression cronExpression) throws Exception {
        schedule(null,null, cronExpression);
    }



    /**
     * 暂停一个触发器
     *
     * @param triggerName
     * @throws RuntimeException
     * @throws SchedulerException
     */
    public boolean pauseTrigger(String triggerName) throws RuntimeException  {
        if (triggerName == null || "".equals(triggerName)) {
            return false;
        }
        System.out.println(triggerName+":禁用");
        try {
            this.scheduler.pauseTrigger(new TriggerKey(triggerName, Scheduler.DEFAULT_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("禁用任务失败");
        }
        return true;
    }

    /**
     * 重启一个触发器
     *
     * @param triggerName
     * @throws RuntimeException
     * @throws SchedulerException
     */
    public boolean resumeTrigger(String triggerName) throws RuntimeException {
        if (triggerName == null || "".equals(triggerName)) {
            return false;
        }
        System.out.println(triggerName+":启用");
        try {
            this.scheduler.resumeTrigger(new TriggerKey(triggerName, Scheduler.DEFAULT_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("重启任务失败");
        }
        return true;
    }

    /**
     * 删除一个触发器
     *
     * @param triggerName
     * @return
     * @throws RuntimeException
     * @throws SchedulerException
     */
    public boolean removeTrigger(String triggerName) throws RuntimeException  {
        try{
            if (triggerName == null || "".equals(triggerName)) {
                return false;
            }
            this.scheduler.pauseTrigger(new TriggerKey(triggerName, Scheduler.DEFAULT_GROUP));
            return this.scheduler.unscheduleJob(new TriggerKey(triggerName,
                    Scheduler.DEFAULT_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("删除任务失败");
        }

    }

    /**
     * 修改触发器的时间
     *
     * @param triggerName
     * @param time
     * @return
     * @throws RuntimeException
     * @throws SchedulerException
     * @throws ParseException
     */
    public boolean modifyJobTime(String triggerName, String time,String taskId) throws RuntimeException{
        try{
            this.removeTrigger(triggerName);
            this.addTrigger(triggerName, time, taskId);
            System.out.println("修改触发器");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("修改任务失败");
        }
    }

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param taskId  任务表主键
     *
     * @param cronExpression
     *            Quartz CronExpression
     * @throws SchedulerException
     */
    public void schedule(String taskId,String tname, CronExpression cronExpression) throws RuntimeException {
        try{
            JobDetail jobDetail = JobBuilder.newJob(MyQuartzJobBean.class).withIdentity(tname,Constant.jobGroup).build();
            jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY,taskId);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(tname, Scheduler.DEFAULT_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                    .build();
            scheduler.scheduleJob(jobDetail,cronTrigger);
        }catch(Exception e){
            throw new RuntimeException("新增定时任务失败");
        }
    }

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     *
     * @param cronExpression
     *            Quartz CronExpression
     * @throws RuntimeException
     */
    public void addschedule(CronExpression cronExpression,String name,String taskId ) throws RuntimeException {
        try {
            JobDetail jobDetail = JobBuilder.newJob(MyQuartzJobBean.class).withIdentity(name,Constant.jobGroup).build();
            jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, taskId);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(name,Scheduler.DEFAULT_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("新增触发器失败");
        }
    }

    /**
     * 在startTime时执行调试一次
     *
     * @param startTime
     *            调度开始时间
     */
    public void schedule(Date startTime) {
        schedule(startTime, null);
    }

    /**
     * 在startTime时执行调试一次
     *
     * @param name
     *            Quartz SimpleTrigger 名称
     * @param startTime
     *            调度开始时间
     */
    public void schedule(String name, Date startTime) {
        schedule(name, startTime, null);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     */
    public void schedule(Date startTime, Date endTime) {
        schedule(startTime, endTime, 0);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param name
     *            Quartz SimpleTrigger 名称
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     */
    public void schedule(String name, Date startTime, Date endTime) {
        schedule(name, startTime, endTime, 0);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     * @param repeatCount
     *            重复执行次数
     */
    public void schedule(Date startTime, Date endTime, int repeatCount) {
        schedule(null, startTime, endTime, 0);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param name
     *            Quartz SimpleTrigger 名称
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     * @param repeatCount
     *            重复执行次数
     */
    public void schedule(String name, Date startTime, Date endTime,
                         int repeatCount) {
        schedule(name, startTime, endTime, 0, 0L);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     * @param repeatCount
     *            重复执行次数
     * @param repeatInterval
     *            执行时间隔间
     */
    public void schedule(Date startTime, Date endTime, int repeatCount,
                         long repeatInterval) {
        schedule(null, startTime, endTime, repeatCount, repeatInterval);
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param name
     *            Quartz SimpleTrigger 名称
     * @param startTime
     *            调度开始时间
     * @param endTime
     *            调度结束时间
     * @param repeatCount
     *            重复执行次数
     * @param repeatInterval
     *            执行时间隔间
     */
    public void schedule(String name, Date startTime, Date endTime,
                         int repeatCount, long repeatInterval) {
        if (name == null || name.trim().equals("")) {
            name = UUID.randomUUID().toString();
        }

        try {
            JobDetail jobDetail = JobBuilder.newJob(MyQuartzJobBean.class).withIdentity(name,Constant.jobGroup).build();
            jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, "");
            scheduler.addJob(jobDetail, true);
            SimpleTrigger SimpleTrigger = TriggerBuilder.newTrigger().withIdentity(name,
                    Scheduler.DEFAULT_GROUP).startAt(startTime).endAt(endTime).withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds((int) repeatInterval)//时间间隔
                            .withRepeatCount(repeatCount)//重复次数（将执行6次）
            ).build();
            scheduler.scheduleJob(SimpleTrigger);
            scheduler.rescheduleJob(new TriggerKey(name, Scheduler.DEFAULT_GROUP),
                    SimpleTrigger);

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
