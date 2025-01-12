package org.bigcai.mpu;

public class InstructionSetArch {

    public static void execInstruction(String instruction, Integer[] registers) {
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
    }
}
