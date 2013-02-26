package com.aifang.apps.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.FactoryBean;

public class ThreadPool implements FactoryBean {
    private int threadNum=10;

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public Object getObject() throws Exception {
        return (ThreadPoolExecutor)Executors.newFixedThreadPool(threadNum);
    }

    @Override
    public Class getObjectType() {
        return ThreadPoolExecutor.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
