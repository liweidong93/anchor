package com.example.anchor;

import android.util.Log;

import com.lwd.anchor.AnchorTask;


/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class AnchorTaskSix extends AnchorTask {

    public AnchorTaskSix() {
        super(ApplicationAnchorTaskCreator.TASK_NAME_SIX);
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public void run() {
        Log.d("lwd", "AnchorTaskSix");
    }



}
