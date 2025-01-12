package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyKernel implements BaseKernel {

    private final Map<String, Integer> processorMap = new ConcurrentHashMap<>();
    AtomicInteger curr = new AtomicInteger(0);

    public final List<TaskStruct> taskStructReadyQueue = new CopyOnWriteArrayList<>();

    public ReentrantLock lockForReadyQueue = new ReentrantLock();

    public MyKernel() {
        // 添加作业
        TaskStruct taskStruct1 = new TaskStruct();
        taskStruct1.setTaskId("taskStruct1");
        taskStruct1.instructions.add("ADD 1 3");
        taskStructReadyQueue.add(taskStruct1);

        TaskStruct taskStruct2 = new TaskStruct();
        taskStruct2.setTaskId("taskStruct2");
        taskStruct2.instructions.add("ADD 3 6");
        taskStructReadyQueue.add(taskStruct2);

        TaskStruct taskStruct3 = new TaskStruct();
        taskStruct3.setTaskId("taskStruct3");
        taskStruct3.instructions.add("ADD 3 8");
        taskStructReadyQueue.add(taskStruct3);

        TaskStruct taskStruct4 = new TaskStruct();
        taskStruct4.setTaskId("taskStruct4");
        taskStruct4.instructions.add("ADD 4 18");
        taskStructReadyQueue.add(taskStruct4);

        TaskStruct taskStruct5 = new TaskStruct();
        taskStruct5.setTaskId("taskStruct5");
        taskStruct5.instructions.add("ADD 5 18");
        taskStructReadyQueue.add(taskStruct5);

        TaskStruct taskStruct6 = new TaskStruct();
        taskStruct6.setTaskId("taskStruct6");
        taskStruct6.instructions.add("ADD 6 18");
        taskStructReadyQueue.add(taskStruct6);
    }

    @Override
    public void init() {
        System.out.println(operatorSystemInfo() + " kernel init ...");
    }

    @Override
    public String operatorSystemInfo() {
        return "[emulation demo of linux]";
    }

    @Override
    public void clockInterrupt(String processorInfo) {
        System.out.println(processorInfo + " kernel code : schedule task of kernel");
        detachTaskByProcessor(processorInfo);
    }

    private void detachTaskByProcessor(String processorInfo) {
        TaskStruct taskStruct = taskStructReadyQueue.get(processorMap.get(processorInfo));
        taskStruct.attachProcessor = null;
        processorMap.remove(processorInfo);
    }

    @Override
    public TaskStruct getCurrentTask(String processorId) {
        if (processorMap.get(processorId) == null) {
            // try retrieve task from processor context is nothing,
            // then try retrieve from kernel ready queue
            tryAttachTaskForProcessor(processorId);
        }

        if(processorMap.get(processorId) == null) {
            // still nothing
            return null;
        } else {
            TaskStruct taskStruct = taskStructReadyQueue.get(processorMap.get(processorId));
            return taskStruct;
        }
    }

    private void tryAttachTaskForProcessor(String processorId) {
        curr = nextTaskIndex();
        int i = curr.get();
        TaskStruct nextTaskStruct = taskStructReadyQueue.get(i);

        lockForReadyQueue.lock();
        if (nextTaskStruct.attachProcessor == null) {
            // distribute a task from ready queue to processor that apply instruction.
            processorMap.put(processorId, i);
            nextTaskStruct.attachProcessor = processorId;
        }
        lockForReadyQueue.unlock();
    }

    private AtomicInteger nextTaskIndex() {
        // schedule next task for processorId
        if (curr.get() >= taskStructReadyQueue.size() - 1) {
            curr.set(0);
        } else {
            curr.incrementAndGet();
        }
        return curr;
    }


}
