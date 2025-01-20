package org.bigcai.mpu;

import org.bigcai.mpu.base.Interrupt;

public class InstructionSetArch {

    public static void execInstruction(String instruction, Integer[] registers, ProcessorUnit processorUnit) {
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
            case "SYSCALL":
                // 软中断实现系统调用
                // 设置特权等级 由用户态 ring3 转为内核态 ring0
                registers[3] = 0;

                doSysCallInterrupt(processorUnit);

                // 中断结束，设置特权等级 由内核态 ring0 转为 用户态ring3
                registers[3] = 3;
                break;
            // 可以添加更多指令
        }
        // 更新程序计数器
        registers[4]+=1;
    }

    private static void doSysCallInterrupt(ProcessorUnit processorUnit) {
        Interrupt interrupt = processorUnit.lapic.getInterruptListeners().get(ProcessorUnit.SYSCALL_INTERRUPT_NUM);
        interrupt.doInterruptJob();
    }


}
