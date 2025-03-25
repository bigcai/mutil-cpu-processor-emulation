package org.bigcai.mpu.interrupt;

import org.bigcai.mpu.base.BaseInterrupt;
import org.bigcai.mpu.base.BaseInterruptController;

public abstract class SyscallBaseInterrupt extends BaseInterrupt {

    private BaseInterruptController interruptOwner;


    public SyscallBaseInterrupt(BaseInterruptController interruptController, Integer interruptNumByInterruptOwner) {
        interruptOwner = interruptController;
        super.interruptNumByInterruptOwner = interruptNumByInterruptOwner;
    }
}
