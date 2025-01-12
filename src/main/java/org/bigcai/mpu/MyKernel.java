package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;

import java.util.ArrayList;
import java.util.List;

public class MyKernel implements BaseKernel {

    int curr = 0;

    @Override
    public void init() {
        System.out.println( operatorSystemInfo() + " kernel init ...");
        // 添加作业
        TaskStruct taskStruct1 = new TaskStruct();
        taskStruct1.instructions.add("ADD 1 3");
        taskStructList.add(taskStruct1);

        TaskStruct taskStruct2 = new TaskStruct();
        taskStruct2.instructions.add("ADD 3 6");
        taskStructList.add(taskStruct2);

        TaskStruct taskStruct3 = new TaskStruct();
        taskStruct3.instructions.add("ADD 3 8");
        taskStructList.add(taskStruct3);
    }

    @Override
    public String operatorSystemInfo() {
        return "[emulation demo of linux]";
    }

    @Override
    public void clockInterrupt() {
        System.out.println("kernel code : schedule task of kernel");
        if(curr >= taskStructList.size()-1) {
            curr = 0;
        } else {
            curr++;
        }
    }

    @Override
    public TaskStruct getCurrentTask() {
        return taskStructList.get(curr);
    }

    public List<TaskStruct> taskStructList = new ArrayList<>();

}
