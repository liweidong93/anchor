package com.lwd.anchor;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public interface OnProjectExecuteListener {

    /**
     * 当project开始执行时调用该函数
     * 该函数在Task所在的线程中回调，注意线程安全
     */
    void onProjectStart();

    /**
     * 当project其中一个task执行结束时，调用该函数
     * 该回调函数在task所在线程中回调，注意线程安全
     * @param taskName
     */
    void onTaskFinish(String taskName);

    /**
     * 当project执行结束时，调用该函数
     */
    void onProjectFinish();

}
