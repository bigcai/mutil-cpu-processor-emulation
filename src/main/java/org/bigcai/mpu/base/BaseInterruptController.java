package org.bigcai.mpu.base;


import java.util.HashMap;
import java.util.Map;


public abstract class BaseInterruptController {
    Map<Integer, BaseInterrupt> interruptListeners = new HashMap<>();

    public void addInterruptListeners(BaseInterrupt interrupt) {
        interruptListeners.put(interrupt.interruptNumByInterruptOwner, interrupt);
    }

    public Map<Integer, BaseInterrupt> getInterruptListeners() {
        return interruptListeners;
    }

    public void callFrom(Integer interruptNum) {
        BaseInterrupt interrupt = interruptListeners.get(interruptNum);
        if (interrupt == null) {
            return;
        }
        doCall(interruptNum);
    }

    public abstract void doCall(Integer interruptNum);
}
