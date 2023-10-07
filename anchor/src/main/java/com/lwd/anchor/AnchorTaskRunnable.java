package com.lwd.anchor;

import android.os.Process;
import android.os.SystemClock;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public class AnchorTaskRunnable implements Runnable {

    private AnchorTask mAnchorTask;
    private com.bitauto.anchor.AnchorProject mAnchorProject;

    public AnchorTaskRunnable(com.bitauto.anchor.AnchorProject anchorProject, AnchorTask anchorTask){
        this.mAnchorTask = anchorTask;
        this.mAnchorProject = anchorProject;
    }

    @Override
    public void run() {
        Process.setThreadPriority(mAnchorTask.priority());
        mAnchorTask.await();
        mAnchorTask.onStart();
        long startTime = SystemClock.elapsedRealtime();
        mAnchorTask.run();
        long executeTime = SystemClock.elapsedRealtime() - startTime;
        mAnchorProject.record(mAnchorTask.getTaskName(), executeTime);
        mAnchorTask.onFinish();
        //通知子任务，当前任务执行完毕了， 相应的计数器要减一
        mAnchorProject.setNotifyChildren(mAnchorTask);
    }
}
