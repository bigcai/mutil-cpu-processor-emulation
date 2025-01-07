package org.bigcai.mpu;

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
                // todo kernel code : schedule task of kernel
                System.out.println("kernel code : schedule task of kernel");
            }
        });

        ClockInterrupt clockInterrupt = (ClockInterrupt)lapic.getInterruptListeners().get(CLOCK_INTERRUPT_NUM);
        if(clockInterrupt != null) {
            int clockInterruptFrequency = PROCESSOR_UNIT_FREQUENCY * 10;
            System.out.println("clock Interrupt Frequency: " + clockInterruptFrequency);
            clockInterrupt.runTimer(clockInterruptFrequency);
        }
    }

    private void loadKernel() {
        // todo 加载内核
    }

    public void loop() {
        while (true) {
            // 处理中断
            if (checkForInterrupt()) {
                handleInterrupt();
            }

            // 执行任务
            executeTasks();
        }
    }

    private void executeTasks() {
        System.out.println("do task");
        try {
            // todo 实现内核的任务队列执行逻辑
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
    }

    private void handleInterrupt() {
        for (Map.Entry<Integer, Boolean> entry: interruptStatus.entrySet()) {
            //  use bit operate would get better performance!
            Integer key = entry.getKey();
            if(entry.getValue() == true) {
                Interrupt interrupt = lapic.getInterruptListeners().get(key);
                interrupt.doInterruptJob();
                // reset
                interruptStatus.put(key, false);
            }
        }
    }

    private boolean checkForInterrupt() {
        boolean existInterrupt = false;
        for (Map.Entry<Integer, Boolean> entry: interruptStatus.entrySet()) {
            //  use bit operate would get better performance!
            if(entry.getValue() == true) {
                existInterrupt = true;
                break;
            }
        }
        return existInterrupt;
    }
}
