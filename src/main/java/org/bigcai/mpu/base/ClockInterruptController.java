package org.bigcai.mpu.base;

import java.util.List;

public class ClockInterruptController {
    List<ClockInterrupt> clockInterruptListeners;

    public void addClockInterruptListeners(ClockInterrupt clockInterrupt) {
        clockInterruptListeners.add(clockInterrupt);
    }

}
