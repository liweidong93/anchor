package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskTwo extends AnchorTask {

    public AnchorTaskTwo() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_TWO);
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskTwo");
    }

    @Override
    public boolean needWait() {
        return true;
    }

}
