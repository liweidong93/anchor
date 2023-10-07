package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskFive extends AnchorTask {

    public AnchorTaskFive() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_FIVE);
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskFive");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
