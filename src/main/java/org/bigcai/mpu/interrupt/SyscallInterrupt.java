package org.bigcai.mpu.interrupt;

import org.bigcai.mpu.base.Interrupt;
import org.bigcai.mpu.base.InterruptController;

public abstract class SyscallInterrupt extends Interrupt {

    private InterruptController interruptOwner;


    public SyscallInterrupt(InterruptController interruptController, Integer interruptNumByInterruptOwner) {
        interruptOwner = interruptController;
        super.interruptNumByInterruptOwner = interruptNumByInterruptOwner;
    }
}
