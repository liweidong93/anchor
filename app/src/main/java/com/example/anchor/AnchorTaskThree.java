package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskThree extends AnchorTask {

    public AnchorTaskThree() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_THREE);
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskThree");
    }



}
