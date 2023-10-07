package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskSeven extends AnchorTask {

    public AnchorTaskSeven() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_SEVEN);
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskSeven");
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
