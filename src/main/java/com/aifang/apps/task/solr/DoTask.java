package com.aifang.apps.task.solr;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aifang.apps.common.Params;
import com.aifang.apps.common.Task;

public class DoTask extends Task {

    protected static Logger logger=Logger.getLogger(DoTask.class);
    @Override
    public Map<String, Object> run(Params params, Map<String, Object> result)
            throws Exception {
        logger.info("current task is:DoTask!");
        String action=(String)params.get("action");
        if("ok".equals(action)){
            System.out.println("do task now...");
        }
        Map<String, Object> rt=new HashMap<String, Object>();
        rt.put("status", true);
        return rt;
    }

}
