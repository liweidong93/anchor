package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskOne extends AnchorTask {

    public AnchorTaskOne() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_ONE);
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskOne");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean needWait() {
        return true;
    }
}
