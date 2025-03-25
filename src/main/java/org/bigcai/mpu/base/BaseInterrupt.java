package org.bigcai.mpu.base;

/**
 *  every type of interrupt should need to declare where is Generated from,
 */
public abstract class BaseInterrupt {
    // interrupt num defined by interrupt owner
    protected Integer interruptNumByInterruptOwner;

    public abstract void doInterruptJob();

    public Integer getInterruptNumByInterruptOwner() {
        return interruptNumByInterruptOwner;
    }
}
