package org.bigcai.mpu.base;

import org.bigcai.mpu.TaskStruct;

public interface BaseKernel {
    public void init();
    public String operatorSystemInfo();


    /**
     * implement schedule function
     *
     * @param processorInfo
     */
    public void clockInterrupt(String processorInfo);

    /**
     * implement ready queue (read task instruction)
     *
     * @param processorId
     * @return
     */
    public TaskStruct getCurrentTask(String processorId);

    /**
     * change task_struct context and run system call of kernel
     *
     * @param processorInfo
     * @param registers
     */
    void syscallInterrupt(String processorInfo , Integer[] registers);
}
