package org.bigcai.mpu.base;


import java.util.HashMap;
import java.util.Map;

public abstract class InterruptController {
    Map<Integer, Interrupt> interruptListeners = new HashMap<>();

    public void addInterruptListeners(Interrupt interrupt) {
        interruptListeners.put(interrupt.interruptNumByInterruptOwner, interrupt);
    }

    public Map<Integer, Interrupt> getInterruptListeners() {
        return interruptListeners;
    }

    public void callFrom(Integer interruptNum) {
        Interrupt interrupt = interruptListeners.get(interruptNum);
        if (interrupt == null) {
            return;
        }
        doCall(interruptNum);
    }

    public abstract void doCall(Integer interruptNum);
}
