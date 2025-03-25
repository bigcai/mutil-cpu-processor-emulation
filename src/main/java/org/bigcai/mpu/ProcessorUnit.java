package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;
import org.bigcai.mpu.base.BaseInterrupt;
import org.bigcai.mpu.interrupt.ClockBaseInterrupt;
import org.bigcai.mpu.interrupt.LocalAdvancedProgrammableBaseInterruptController;
import org.bigcai.mpu.interrupt.SyscallBaseInterrupt;

import java.util.HashMap;
import java.util.Map;

public class ProcessorUnit {
    public static final int CLOCK_INTERRUPT_NUM = 0x20;
    public static final int SYSCALL_INTERRUPT_NUM = 0x80;
    public static final int PROCESSOR_UNIT_FREQUENCY = 100;
    public static final int PROGRAM_COUNTOR_REGISTER = 4;
    public static final int RETURN_VALUE_REGISTER = 0;
    public static final int INSTRUCTION_FIRST_ARG_REGISTER = 1;
    public static final int INSTRUCTION_SECOND_ARG_REGISTER = 2;
    public static final int CURRENT_PRIVILEGE_LEVEL_REGISTER = 3;
    public static final int CURRENT_PRIVILEGE_LEVEL_KERNEL_SPACE = 0;
    public static final int CURRENT_PRIVILEGE_LEVEL_USER_SPACE = 3;

    public Map<Integer, Boolean> interruptStatus = new HashMap<>();

    LocalAdvancedProgrammableBaseInterruptController lapic = new LocalAdvancedProgrammableBaseInterruptController(this);

    private Integer[] registers = new Integer[5];  // 模拟寄存器
    private String processorInfo;
    private BaseKernel baseKernel;
    public int frequency;

    public ProcessorUnit(String processorInfo, BaseKernel baseKernel, int frequency) {
        this.processorInfo = processorInfo;
        this.baseKernel = baseKernel;
        this.frequency = frequency;
    }

    public void powerOn() {
        // clock interrupt need run timer from unit of processor when power on processor .
        init();
        loop();
    }

    private void init() {

        loadKernel();

        // register a clock interrupt, to schedule task struct
        lapic.addInterruptListeners(new ClockBaseInterrupt(lapic, CLOCK_INTERRUPT_NUM) {
            @Override
            public void doInterruptJob() {
                // kernel code : schedule task of kernel
                baseKernel.clockInterrupt(processorInfo);
            }
        });

        lapic.addInterruptListeners(new SyscallBaseInterrupt(lapic, SYSCALL_INTERRUPT_NUM) {
            @Override
            public void doInterruptJob() {
                // kernel code : do syscall
                baseKernel.syscallInterrupt(processorInfo, registers);
            }
        });

        ClockBaseInterrupt clockInterrupt = (ClockBaseInterrupt) lapic.getInterruptListeners().get(CLOCK_INTERRUPT_NUM);
        if (clockInterrupt != null) {
            int clockInterruptFrequency = PROCESSOR_UNIT_FREQUENCY * frequency;
            System.out.println(processorInfo + " clock Interrupt Frequency: " + clockInterruptFrequency);
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
            // 处理中断
            if (checkForInterrupt()) {
                handleInterrupt();
            }
            // 执行任务
            executeInstruction();
        }
    }

    private void executeInstruction() {
        // 从内核加载指令,  elf 文件，变成 task_struct , task_struct 编译成指令集合， task_struct 包含了环境变量和质量，
        // task_struct 包含了时间片计数器（其实就是指令数计数器）
        TaskStruct task = baseKernel.getCurrentTask(processorInfo);

        if(task == null || task.attachProcessor == null || !task.attachProcessor.equals(processorInfo)) {
            // 模拟指令耗时
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return;
        }
        registers[PROGRAM_COUNTOR_REGISTER] = task.programCounter;

        if(registers[PROGRAM_COUNTOR_REGISTER] >= task.instructions.size()
                || task.instructions.get(registers[PROGRAM_COUNTOR_REGISTER]) == null) {
            // if program counter point the end of program, or nothing instruction need to run , just do nothing.
            return;
        }
        String[] instruction = task.instructions.get(registers[4]).split(" ");
        String command = instruction[0];

        registers[RETURN_VALUE_REGISTER] = 0;
        registers[INSTRUCTION_FIRST_ARG_REGISTER] = Integer.valueOf(instruction[INSTRUCTION_FIRST_ARG_REGISTER]);
        registers[INSTRUCTION_SECOND_ARG_REGISTER] = Integer.valueOf(instruction[INSTRUCTION_SECOND_ARG_REGISTER]);
        // 设置特权等级为 用户态
        registers[CURRENT_PRIVILEGE_LEVEL_REGISTER] = 3;
        // 设置程序计数器
        doInstruction(command, "taskId is " + task.taskId);
        task.programCounter = registers[PROGRAM_COUNTOR_REGISTER];

        if(task.programCounter >= task.instructions.size() || task.instructions.get(task.programCounter) == null) {
            System.out.println("taskId " + task.taskId + " is end!");
        }
    }

    // 执行任务中的每条指令
    private void doInstruction(String instruction, String debugInfo) {
        // 处理中断
        if (checkForInterrupt()) {
            handleInterrupt();
            return;
        }
        InstructionSetArch.execInstruction(instruction, registers, this);

        System.out.println(processorInfo + " 指令执行完毕: " + instruction + " 寄存器状态 " + registers[0]  + " debuginfo: " + debugInfo);
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
                BaseInterrupt interrupt = lapic.getInterruptListeners().get(key);
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
