package com.lwd.anchor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author: liweidong
 * @data: 2023/9/22
 */
public class AnchorTaskUtils {

    public static List<AnchorTask> getSortAnchorTaskResult(List<AnchorTask> list, Map<String, AnchorTask> taskMap
            , Map<String, List<AnchorTask>> taskChildMap){
        List<AnchorTask> result = new ArrayList<>();
        Queue<AnchorTask> queue = new LinkedList<>();
        Map<String, Integer> taskIntegerMap = new HashMap<>();
        //建立每个task的入度关系
        for (AnchorTask task : list){
            String taskName = task.getTaskName();
            if (taskIntegerMap.containsKey(taskName)){
                throw new com.bitauto.anchor.AnchorTaskException("anchorTask is repeat, anchorTask is $anchorTask, list is $list");
            }
            int size = task.getDependsTaskList().size();
            taskIntegerMap.put(taskName, size);
            taskMap.put(taskName, task);
            if (size == 0){
                queue.offer(task);
            }
        }
        //建立每个task的children关系
        for (AnchorTask task : list){
            if (task.getDependsTaskList() != null && task.getDependsTaskList().size() != 0){
                for (String childTaskName : task.getDependsTaskList()){
                    List<AnchorTask> anchorTasks = taskChildMap.get(childTaskName);
                    if (anchorTasks == null){
                        anchorTasks = new ArrayList<>();
                    }
                    anchorTasks.add(task);
                    taskChildMap.put(childTaskName, anchorTasks);
                }
            }
        }
        while (!queue.isEmpty()){
            AnchorTask poll = queue.poll();
            result.add(poll);
            if (taskChildMap.get(poll.getTaskName()) != null){
                for (AnchorTask anchorTask : taskChildMap.get(poll.getTaskName())){
                    if (taskIntegerMap.containsKey(anchorTask.getTaskName())){
                        int integer = taskIntegerMap.get(anchorTask.getTaskName());
                        integer--;
                        if (integer == 0){
                            queue.offer(anchorTask);
                        }
                        taskIntegerMap.put(anchorTask.getTaskName(), integer);
                    }
                }
            }
        }
        if (list.size() != result.size()){
            throw new com.bitauto.anchor.AnchorTaskException("anchorTask is repeat, anchorTask is $anchorTask, list is $list");
        }
        return result;
    }

}
