package com.lwd.anchor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class ExecuteMonitor {

    private ConcurrentHashMap<String, Long> mExecuteTimeMap = new ConcurrentHashMap<>();
    private long mStartTime = 0L;
    //返回project的执行时间
    public long projectCostTime = 0L;
    private Handler mHandler;

    /**
     * 记录task的执行时间
     * @param taskName
     * @param executeTime
     */
    public void record(String taskName, long executeTime){
        mExecuteTimeMap.put(taskName, executeTime);
    }

    /**
     * 已执行完的每个task的执行时间
     * @return
     */
    public Map<String, Long> executeTimeMap(){
        return mExecuteTimeMap;
    }

    /**
     * 在project开始执行时的打点，记录开始时间
     */
    public void recordProjectStart(){
        mStartTime = System.currentTimeMillis();
    }

    /**
     * 在project执行结束时的打点，记录耗时
     */
    public void recordProjectFinish(){
        projectCostTime = System.currentTimeMillis() - mStartTime;
    }

}
