package com.bonaparte.service;

import com.bonaparte.entity.QuartzTask;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public interface IQuartzTaskSrv {
    boolean matchedTask(QuartzTask task);

    void runTask(QuartzTask task);
}
