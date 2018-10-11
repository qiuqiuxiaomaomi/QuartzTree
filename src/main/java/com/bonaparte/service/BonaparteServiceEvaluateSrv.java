package com.bonaparte.service;

import com.bonaparte.entity.QuartzTask;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public class BonaparteServiceEvaluateSrv implements IQuartzTaskSrv{
    private static Logger logger = LoggerFactory.getLogger(BonaparteServiceEvaluateSrv.class);
    public static RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(50000).setSocketTimeout(150000).build();
    static String taskRemark = "serviceEvaluate";

    @Override
    public boolean matchedTask(QuartzTask task) {
        if (taskRemark.equals(task.getRemark())){
            return true;
        }
        return false;
    }

    @Override
    public void runTask(QuartzTask task) {

    }
}
