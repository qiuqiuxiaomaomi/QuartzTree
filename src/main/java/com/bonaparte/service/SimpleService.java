package com.bonaparte.service;

import com.bonaparte.dao.mapper.QuartzTaskMapper;
import com.bonaparte.entity.QuartzTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by yangmingquan on 2018/10/11.
 */
@Service
public class SimpleService implements ApplicationContextAware {


    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public QuartzTaskMapper quartzTaskMapper;

    Map<String, IQuartzTaskSrv> impls = null;

    public void jobMethod(String taskId) {
        QuartzTask quartzTask = new QuartzTask();
        quartzTask.setTaskId(taskId);
        quartzTask = quartzTaskMapper.selectOne(quartzTask);

        for (Map.Entry<String, IQuartzTaskSrv> impl : impls.entrySet()) {
            logger.info(impl.getKey(), impl.getValue().getClass().getName());
            IQuartzTaskSrv ins = impl.getValue();
            if (ins.matchedTask(quartzTask)) {
                ins.runTask(quartzTask);
            }
        }
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        Map<String, IQuartzTaskSrv> impls = applicationContext.getBeansOfType(IQuartzTaskSrv.class);
        this.impls = impls;
    }
}
