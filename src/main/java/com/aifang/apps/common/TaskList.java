package com.aifang.apps.common;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskList {
    private List<Task> taskList=new LinkedList<Task>();

    private ThreadPoolExecutor threadPoolExecutor;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void run(final Params params){
        if(null!=taskList){
            threadPoolExecutor.execute(new Runnable() {

                public void run() {
                    for(Task task:taskList){
                        try{
                            task.start(params);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void mydestroy(){
        if(null!=threadPoolExecutor){
            threadPoolExecutor.shutdown();
        }
    }

    public void myinit(){
    }
}
