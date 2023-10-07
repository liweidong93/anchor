package com.lwd.anchor;

import android.os.Process;

import com.lwd.anchor.IAnchorTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: liweidong
 * @data: 2023/9/22
 */
public abstract class AnchorTask implements IAnchorTask {

    private CountDownLatch countDownLatch;
    private String mTaskName;
    private List<String> mDependList = new ArrayList<>();
    private List<IAnchorCallBack> mList = new ArrayList<>();

    public AnchorTask(String name){
        this.mTaskName = name;
    }

    private int getListSize(){
        return getDependsTaskList() != null ? getDependsTaskList().size() : 0;
    }

    @Override
    public String getTaskName() {
        return mTaskName;
    }

    @Override
    public int priority() {
        return Process.THREAD_PRIORITY_FOREGROUND;
    }

    @Override
    public boolean needWait() {
        return true;
    }

    public void afterTask(String taskName){
        mDependList.add(taskName);
    }

    public void await(){
        try {
            tryToInitCountDown();
            countDownLatch.await();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void tryToInitCountDown(){
        if (countDownLatch == null){
            countDownLatch = new CountDownLatch(mDependList.size());
        }
    }

    public void countDown(){
        tryToInitCountDown();
        countDownLatch.countDown();
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    public List<String> getDependsTaskList() {
        return mDependList;
    }

    @Override
    public void onAdd() {
        for (IAnchorCallBack callBack : mList){
            callBack.onAdd();
        }
    }

    @Override
    public void onStart() {
        for (IAnchorCallBack callBack : mList){
            callBack.onStart();
        }
    }

    @Override
    public void onFinish() {
        for (IAnchorCallBack callBack : mList){
            callBack.onFinish();
        }
    }

    public void addCallBack(IAnchorCallBack iAnchorCallBack){
        if (iAnchorCallBack == null){
            return;
        }
        mList.add(iAnchorCallBack);
    }

    public void removeCallBack(IAnchorCallBack anchorCallBack){
        if (anchorCallBack == null){
            return;
        }
        mList.remove(anchorCallBack);
    }
}
