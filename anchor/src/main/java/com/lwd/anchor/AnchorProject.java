package com.bitauto.anchor;

import android.content.Context;

import com.lwd.anchor.AnchorTask;
import com.lwd.anchor.AnchorTaskRunnable;
import com.lwd.anchor.AnchorTaskUtils;
import com.lwd.anchor.ExecuteMonitor;
import com.lwd.anchor.IAnchorTaskCreator;
import com.lwd.anchor.OnGetMonitorRecordCallback;
import com.lwd.anchor.OnProjectExecuteListener;
import com.lwd.anchor.TaskCreatorWrap;
import com.lwd.anchor.TaskExecutorManager;
import com.lwd.anchor.ThreadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public class AnchorProject {

    private ThreadPoolExecutor threadPoolExecutor;
    //存储所有的任务，key是taskName，value是AnchorTask
    private Map<String, AnchorTask> taskMap;
    //存储当前任务的子任务，key是当前任务的taskName，value是AnchorTask的list
    private Map<String, List<AnchorTask>> taskChildMap;
    //需要等待任务的总数，用于阻塞
    private CountDownLatch countDownLatch;
    //拓扑排序后的主线程任务
    private List<AnchorTask> mainList;
    //拓扑排序后的子线程任务
    private List<AnchorTask> threadList;
    private int totalTaskSize;
    private AtomicInteger finishTask = new AtomicInteger(0);
    private List<OnProjectExecuteListener> listeners = new ArrayList<>();
    private IAnchorTaskCreator iAnchorTaskCreator;
    private AnchorTask cacheTask;
    private ExecuteMonitor executeMonitor = new ExecuteMonitor();
    private OnGetMonitorRecordCallback onGetMonitorRecordCallback;
    private Context context;
    private long timeOutMillion;

    public AnchorProject(Builder builder){
        threadPoolExecutor = builder.threadPoolExecutor != null ? builder.threadPoolExecutor : TaskExecutorManager.getInstance().cpuThreadPoolExecutor;
        taskMap = builder.taskMap;
        taskChildMap = builder.taskChildMap;
        countDownLatch = builder.countDownLatch;
        mainList = builder.mainList;
        threadList = builder.threadList;
        totalTaskSize = builder.list.size();
        iAnchorTaskCreator = builder.iAnchorTaskCreator;
        cacheTask = builder.cacheTask;
        onGetMonitorRecordCallback = builder.recordCallback;
        context = builder.context;
        timeOutMillion = builder.timeOutMillion;
    }

    public void addListener(OnProjectExecuteListener listener){
        listeners.add(listener);
    }

    public void removeListener(OnProjectExecuteListener listener){
        listeners.remove(listener);
    }

    public void record(String taskName, long executeTime){
        executeMonitor.record(taskName, executeTime);
    }

    /**
     * 通知child countdown，当前的阻塞任务也需要countdown
     * @param anchorTask
     */
    public void setNotifyChildren(AnchorTask anchorTask){
        if (taskChildMap.get(anchorTask.getTaskName()) != null){
            for (AnchorTask task : taskChildMap.get(anchorTask.getTaskName())){
                if (taskMap.get(task.getTaskName()) != null){
                    taskMap.get(task.getTaskName()).countDown();
                }
            }
        }
        if (anchorTask.needWait()){
            countDownLatch.countDown();
        }
        for (OnProjectExecuteListener listener : listeners){
            listener.onTaskFinish(anchorTask.getTaskName());
        }
        finishTask.incrementAndGet();
        if (finishTask.get() == totalTaskSize){
            executeMonitor.recordProjectFinish();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (onGetMonitorRecordCallback != null){
                        onGetMonitorRecordCallback.onGetTaskExecuteRecord(executeMonitor.executeTimeMap());
                        onGetMonitorRecordCallback.onGetProjectExecuteTime(executeMonitor.projectCostTime);
                    }
                }
            });
            for (OnProjectExecuteListener listener : listeners){
                listener.onProjectFinish();
            }
        }
    }

    public AnchorProject start(){
        executeMonitor.recordProjectStart();
        for (OnProjectExecuteListener listener : listeners){
            listener.onProjectStart();
        }
        for (AnchorTask anchorTask : threadList){
            threadPoolExecutor.execute(new AnchorTaskRunnable(this, anchorTask));
        }
        for (AnchorTask anchorTask : mainList){
            new AnchorTaskRunnable(this, anchorTask).run();
        }
        return this;
    }

    public void await(long timeOutMillion){
        try{
            this.timeOutMillion = timeOutMillion;
            if (timeOutMillion > 0){
                countDownLatch.await(timeOutMillion, TimeUnit.MILLISECONDS);
            }else{
                countDownLatch.await();
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public void await(){
        try{
            if (timeOutMillion > 0){
                await(timeOutMillion);
            }else{
                await(0);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static class Builder {
        private ThreadPoolExecutor threadPoolExecutor;
        private Context context;
        private long timeOutMillion;
        //存储所有的任务
        private List<AnchorTask> list = new ArrayList<>();
        //存储所有的任务，key是taskName，value是AnchorTask
        private Map<String, AnchorTask> taskMap = new HashMap<>();
        //存储当前任务的子任务，key是当前任务taskName，value是AnchorTask的集合
        private Map<String, List<AnchorTask>> taskChildMap = new HashMap<>();
        //拓扑排序后的主线程任务
        private List<AnchorTask> mainList = new ArrayList<>();
        //拓扑排序后的子线程任务
        private List<AnchorTask> threadList = new ArrayList<>();
        //需要等待的任务总数，用于阻塞
        private CountDownLatch countDownLatch;
        //需要等待的任务总数，用于CountDownLatch
        private AtomicInteger needWaitCount = new AtomicInteger();
        private IAnchorTaskCreator iAnchorTaskCreator = null;
        private TaskCreatorWrap taskCreatorWrapper = new TaskCreatorWrap(iAnchorTaskCreator);
        private AnchorTask cacheTask = null;
        private OnGetMonitorRecordCallback recordCallback;

        public Builder setAnchorTaskCreator(IAnchorTaskCreator iAnchorTaskCreator){
            this.iAnchorTaskCreator = iAnchorTaskCreator;
            taskCreatorWrapper.mCreator = iAnchorTaskCreator;
            return this;
        }

        public Builder setOnGetMonitorRecordCallback(OnGetMonitorRecordCallback callback){
            this.recordCallback = callback;
            return this;
        }

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }

        public Builder setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor){
            this.threadPoolExecutor = threadPoolExecutor;
            return this;
        }

        public Builder setTimeOutMillion(long timeOutMillion){
            this.timeOutMillion = timeOutMillion;
            return this;
        }

        public Builder addTask(String taskName){
            AnchorTask task = taskCreatorWrapper.createTask(taskName);
            if (task == null){
                throw new com.bitauto.anchor.AnchorTaskException("could not find anchorTask, taskName is $taskName");
            }
            return addTask(task);
        }

        public Builder addTask(AnchorTask anchorTask){
            cacheTask = anchorTask;
            list.add(anchorTask);
            anchorTask.onAdd();
            if (anchorTask.needWait()){
                needWaitCount.incrementAndGet();
            }
            return this;
        }

        public Builder afterTask(String... taskNames){
            if (cacheTask == null){
                throw new com.bitauto.anchor.AnchorTaskException("should be call addTask first");
            }
            for (String name : taskNames){
                AnchorTask task = taskCreatorWrapper.createTask(name);
                if (task == null){
                    throw new com.bitauto.anchor.AnchorTaskException("could not find anchorTask, taskName is $taskName");
                }
                if (cacheTask != null){
                    cacheTask.afterTask(name);
                }
            }
            return this;
        }

        public Builder afterTask(AnchorTask... tasks){
            if (tasks == null){
                throw new com.bitauto.anchor.AnchorTaskException("should be call addTask first");
            }
            for (AnchorTask anchorTask : tasks){
                if (anchorTask != null){
                    cacheTask.afterTask(anchorTask.getTaskName());
                }
            }
            return this;
        }

        public AnchorProject build(){
            List<AnchorTask> sortAnchorTaskResult = AnchorTaskUtils.getSortAnchorTaskResult(list, taskMap, taskChildMap);
            if (sortAnchorTaskResult != null && sortAnchorTaskResult.size() > 0){
                for (AnchorTask anchorTask : sortAnchorTaskResult){
                    if (anchorTask != null){
                        if (anchorTask.isRunOnMainThread()){
                            mainList.add(anchorTask);
                        }else{
                            threadList.add(anchorTask);
                        }
                    }
                }
                countDownLatch = new CountDownLatch(needWaitCount.get());
            }
            return new AnchorProject(this);
        }
    }

}
