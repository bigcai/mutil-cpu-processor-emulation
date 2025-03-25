package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseInterrupt;

import static org.bigcai.mpu.ProcessorUnit.CURRENT_PRIVILEGE_LEVEL_KERNEL_SPACE;
import static org.bigcai.mpu.ProcessorUnit.CURRENT_PRIVILEGE_LEVEL_USER_SPACE;

public class InstructionSetArch {

    public static void execInstruction(String instruction, Integer[] registers, ProcessorUnit processorUnit) {
        // 根据指令执行对应操作（这里简化为加法操作）
        switch (instruction) {
            case "ADD":
                registers[ProcessorUnit.RETURN_VALUE_REGISTER]
                        = registers[ProcessorUnit.INSTRUCTION_FIRST_ARG_REGISTER]
                        + registers[ProcessorUnit.INSTRUCTION_SECOND_ARG_REGISTER];
                break;
            case "SUB":
                registers[ProcessorUnit.RETURN_VALUE_REGISTER]
                        = registers[ProcessorUnit.INSTRUCTION_FIRST_ARG_REGISTER]
                        - registers[ProcessorUnit.INSTRUCTION_SECOND_ARG_REGISTER];
                break;
            case "MUL":
                registers[ProcessorUnit.RETURN_VALUE_REGISTER]
                        = registers[ProcessorUnit.INSTRUCTION_FIRST_ARG_REGISTER]
                        * registers[ProcessorUnit.INSTRUCTION_SECOND_ARG_REGISTER];
                break;
            case "SYSCALL":
                // 软中断实现系统调用
                // 设置特权等级 由用户态 ring3 转为内核态 ring0
                registers[ProcessorUnit.CURRENT_PRIVILEGE_LEVEL_REGISTER] = CURRENT_PRIVILEGE_LEVEL_KERNEL_SPACE;

                doSysCallInterrupt(processorUnit);

                // 中断结束，设置特权等级 由内核态 ring0 转为 用户态ring3
                registers[ProcessorUnit.CURRENT_PRIVILEGE_LEVEL_REGISTER] = CURRENT_PRIVILEGE_LEVEL_USER_SPACE;
                break;
            // 可以添加更多指令
        }
        // 更新程序计数器
        registers[ProcessorUnit.PROGRAM_COUNTOR_REGISTER]+=1;
    }

    private static void doSysCallInterrupt(ProcessorUnit processorUnit) {
        BaseInterrupt interrupt = processorUnit.lapic.getInterruptListeners().get(ProcessorUnit.SYSCALL_INTERRUPT_NUM);
        interrupt.doInterruptJob();
    }


}
