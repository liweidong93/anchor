package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskFour extends AnchorTask {

    public AnchorTaskFour() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_FOUR);
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskFour");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean needWait() {
        return false;
    }
}
