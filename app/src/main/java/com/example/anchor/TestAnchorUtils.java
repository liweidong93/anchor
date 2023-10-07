package com.example.anchor;

import android.content.Context;
import android.util.Log;

import com.bitauto.anchor.AnchorProject;
import com.lwd.anchor.OnGetMonitorRecordCallback;
import com.lwd.anchor.OnProjectExecuteListener;

import java.util.Map;

/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class TestAnchorUtils {

    public static void execute(Context context){
        AnchorProject anchorProject =
                new AnchorProject
                        .Builder()
                        .setContext(context)
                        .setAnchorTaskCreator(new ApplicationAnchorTaskCreator())
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_ONE)
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_TWO)
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_THREE).afterTask(
                                ApplicationAnchorTaskCreator.TASK_NAME_TWO
                        )
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_FOUR)
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_FIVE).afterTask(
                                ApplicationAnchorTaskCreator.TASK_NAME_ONE, ApplicationAnchorTaskCreator.TASK_NAME_TWO
                        )
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_SIX)
                        .addTask(ApplicationAnchorTaskCreator.TASK_NAME_SEVEN).afterTask(
                                ApplicationAnchorTaskCreator.TASK_NAME_FIVE, ApplicationAnchorTaskCreator.TASK_NAME_THREE
                        )
                        .setOnGetMonitorRecordCallback(new OnGetMonitorRecordCallback() {
                            @Override
                            public void onGetTaskExecuteRecord(Map<String, Long> result) {
                                Log.d("lwd", "onGetTaskExecuteRecord taskTime:" + result.toString());
                            }

                            @Override
                            public void onGetProjectExecuteTime(long costTime) {
                                Log.d("lwd", "onGetProjectExecuteTime projectTime:" + costTime);
                            }
                        })
                        .build();
        anchorProject.addListener(new OnProjectExecuteListener() {
            @Override
            public void onProjectStart() {
                Log.d("lwd", "onProjectStart");
            }

            @Override
            public void onTaskFinish(String taskName) {
                Log.d("lwd", "onTaskFinish taskName：" + taskName);
            }

            @Override
            public void onProjectFinish() {
                Log.d("lwd", "onProjectFinish");
            }
        });
        anchorProject.start().await();
    }

}
