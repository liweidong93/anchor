package com.lwd.anchor;

import java.util.Map;

/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public interface OnGetMonitorRecordCallback {

    /**
     * 获取task执行耗时
     * key执行task名称，value执行耗时，单位毫秒
     * @param result
     */
    void onGetTaskExecuteRecord(Map<String, Long> result);

    /**
     * 获取整个project执行耗时，单位毫秒
     * @param costTime
     */
    void onGetProjectExecuteTime(long costTime);

}
