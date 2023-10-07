package com.lwd.anchor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public class TaskCreatorWrap implements IAnchorTaskCreator {

    public IAnchorTaskCreator mCreator;

    public TaskCreatorWrap(IAnchorTaskCreator iAnchorTaskCreator){
        this.mCreator = iAnchorTaskCreator;
    }

    private Map<String, AnchorTask> map = new HashMap<>();

    @Override
    public AnchorTask createTask(String taskName) {
        if (map.containsKey(taskName)){
            AnchorTask task = map.get(taskName);
            return task;
        }
        if (mCreator != null){
            return mCreator.createTask(taskName);
        }
        return null;
    }

    public boolean checkTaskIsExits(String taskName){
        return map.containsKey(taskName);
    }
}
