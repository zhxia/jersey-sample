package com.aifang.apps.task.solr;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aifang.apps.common.Params;
import com.aifang.apps.common.Task;

public class DoTask2 extends Task {

    protected static Logger logger=Logger.getLogger(DoTask2.class);
    @Override
    public Map<String, Object> run(Params params, Map<String, Object> result)
            throws Exception {
        logger.info("current task is:DoTask2");
        return null;
    }

}
