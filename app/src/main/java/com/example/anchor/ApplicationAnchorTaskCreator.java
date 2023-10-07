package com.example.anchor;

import com.lwd.anchor.AnchorTask;
import com.lwd.anchor.IAnchorTaskCreator;

/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class ApplicationAnchorTaskCreator implements IAnchorTaskCreator {

    public static final String TASK_NAME_ONE = "TASK_NAME_ONE";
    public static final String TASK_NAME_TWO = "TASK_NAME_TWO";
    public static final String TASK_NAME_THREE = "TASK_NAME_THREE";
    public static final String TASK_NAME_FOUR = "TASK_NAME_FOUR";
    public static final String TASK_NAME_FIVE = "TASK_NAME_FIVE";
    public static final String TASK_NAME_SIX = "TASK_NAME_SIX";
    public static final String TASK_NAME_SEVEN = "TASK_NAME_SEVEN";

    @Override
    public AnchorTask createTask(String taskName) {
        switch (taskName){
            case TASK_NAME_ONE:
                return new AnchorTaskOne();
            case TASK_NAME_TWO:
                return new AnchorTaskTwo();
            case TASK_NAME_THREE:
                return new AnchorTaskThree();
            case TASK_NAME_FOUR:
                return new AnchorTaskFour();
            case TASK_NAME_FIVE:
                return new AnchorTaskFive();
            case TASK_NAME_SIX:
                return new AnchorTaskSix();
            case TASK_NAME_SEVEN:
                return new AnchorTaskSeven();
        }
        return null;
    }
}
