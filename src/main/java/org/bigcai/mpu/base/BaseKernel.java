package org.bigcai.mpu.base;

import org.bigcai.mpu.TaskStruct;

public interface BaseKernel {
    public void init();
    public String operatorSystemInfo();
    public void clockInterrupt();

    public TaskStruct getCurrentTask();
}
