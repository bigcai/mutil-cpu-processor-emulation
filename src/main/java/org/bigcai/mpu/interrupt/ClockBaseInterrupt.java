package org.bigcai.mpu.interrupt;

import org.bigcai.mpu.base.BaseInterrupt;
import org.bigcai.mpu.base.BaseInterruptController;

import java.util.Timer;
import java.util.TimerTask;

public abstract class ClockBaseInterrupt extends BaseInterrupt {
    // this clock interrupt is generated from timer that schedule at fixed rate.
    public Timer timer  = new Timer();

    private BaseInterruptController interruptOwner;


    public ClockBaseInterrupt(BaseInterruptController interruptController, Integer interruptNumByInterruptOwner) {
        interruptOwner = interruptController;
        super.interruptNumByInterruptOwner = interruptNumByInterruptOwner;
    }

    public void runTimer(int period) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // timer to call owner of clock interrupt
                interruptOwner.callFrom(ClockBaseInterrupt.super.getInterruptNumByInterruptOwner());
            }
        };

        // 每隔1000毫秒（1秒）执行一次
        timer.scheduleAtFixedRate(task, 0, period);
    }

}
