package org.bigcai.mpu;

import java.util.ArrayList;
import java.util.List;

public class TaskStruct {

    String taskId;
    String attachProcessor;

    Integer programCounter = 0;


    public List<String> instructions = new ArrayList<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
