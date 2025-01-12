package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;
import org.bigcai.mpu.base.Interrupt;
import org.bigcai.mpu.interrupt.ClockInterrupt;
import org.bigcai.mpu.interrupt.LocalAdvancedProgrammableInterruptController;

import java.util.HashMap;
import java.util.Map;

public class ProcessorUnit {
    public static final int CLOCK_INTERRUPT_NUM = 999;
    public static final int PROCESSOR_UNIT_FREQUENCY = 100;

    public Map<Integer, Boolean> interruptStatus = new HashMap<>();

    LocalAdvancedProgrammableInterruptController lapic = new LocalAdvancedProgrammableInterruptController(this);

    private Integer[] registers = new Integer[4];  // 模拟寄存器

    private String processorInfo;
    private BaseKernel baseKernel;

    public ProcessorUnit(String processorInfo, BaseKernel baseKernel) {
        this.processorInfo = processorInfo;
        this.baseKernel = baseKernel;
    }

    public void powerOn() {
        // clock interrupt need run timer from unit of processor when power on processor .
        init();
        loop();
    }

    private void init() {

        loadKernel();

        // register a clock interrupt, to schedule task struct
        lapic.addInterruptListeners(new ClockInterrupt(lapic, CLOCK_INTERRUPT_NUM) {
            @Override
            public void doInterruptJob() {
                // kernel code : schedule task of kernel
                baseKernel.clockInterrupt();
            }
        });

        ClockInterrupt clockInterrupt = (ClockInterrupt) lapic.getInterruptListeners().get(CLOCK_INTERRUPT_NUM);
        if (clockInterrupt != null) {
            int clockInterruptFrequency = PROCESSOR_UNIT_FREQUENCY * 10;
            System.out.println("clock Interrupt Frequency: " + clockInterruptFrequency);
            clockInterrupt.runTimer(clockInterruptFrequency);
        }
    }

    private void loadKernel() {
        // load kernel , and initial param of os
        System.out.println("Processor " + processorInfo + " work for " + baseKernel.operatorSystemInfo() + " loading...");
        baseKernel.init();
        System.out.println("Processor " + processorInfo + " work for " + baseKernel.operatorSystemInfo() + " load finish!");
    }

    public void loop() {
        while (true) {
            // 执行任务
            executeInstruction();
        }
    }

    private void executeInstruction() {
        // 从内核加载指令,  elf 文件，变成 task_struct , task_struct 编译成指令集合， task_struct 包含了环境变量和质量，
        // task_struct 包含了时间片计数器（其实就是指令数计数器）
        TaskStruct task = baseKernel.getCurrentTask();
        String[] instruction = task.instructions.get(0).split(" ");
        registers[0] = 0;
        registers[1] = Integer.valueOf(instruction[1]);
        registers[2] = Integer.valueOf(instruction[2]);
        doInstruction(instruction[0]);
    }

    // 执行任务中的每条指令
    private void doInstruction(String instruction) {
        // 处理中断
        if (checkForInterrupt()) {
            handleInterrupt();
            return;
        }

        // 根据指令执行对应操作（这里简化为加法操作）
        switch (instruction) {
            case "ADD":
                registers[0] = registers[1] + registers[2];
                break;
            case "SUB":
                registers[0] = registers[1] - registers[2];
                break;
            case "MUL":
                registers[0] = registers[1] * registers[2];
                break;
            // 可以添加更多指令
        }
        System.out.println("指令执行完毕: " + instruction + " 寄存器状态 " + registers[0]);

        // 模拟指令耗时
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    private void handleInterrupt() {
        for (Map.Entry<Integer, Boolean> entry : interruptStatus.entrySet()) {
            //  use bit operate would get better performance!
            Integer key = entry.getKey();
            if (entry.getValue() == true) {
                Interrupt interrupt = lapic.getInterruptListeners().get(key);
                interrupt.doInterruptJob();
                // reset
                interruptStatus.put(key, false);
            }
        }
    }

    private boolean checkForInterrupt() {
        boolean existInterrupt = false;
        for (Map.Entry<Integer, Boolean> entry : interruptStatus.entrySet()) {
            //  use bit operate would get better performance!
            if (entry.getValue() == true) {
                existInterrupt = true;
                break;
            }
        }
        return existInterrupt;
    }
}
