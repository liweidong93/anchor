package com.lwd.anchor;

import java.util.List;

/**
 * @author: liweidong
 * @data: 2023/9/22
 */
public interface IAnchorTask extends IAnchorCallBack {

    /**
     * 是否在主线程执行，默认false
     * @return
     */
    boolean isRunOnMainThread();

    /**
     * 线程优先级，默认值是 Process.THREAD_PRIORITY_FOREGROUND
     * @return
     */
    int priority();

    /**
     * 调用await方法时，是否需要等待，true:需要等待，表示该任务执行结束才能继续往下执行
     * @return
     */
    boolean needWait();

    /**
     * 返回该方法前置任务依赖，默认值返回null
     * @return
     */
//    List<Class<? extends AnchorTask>> getDependsTaskList();

    /**
     * 任务执行
     */
    void run();

    /**
     * 获取任务昵称
     * @return
     */
    String getTaskName();

}
