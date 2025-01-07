package org.bigcai.mpu.interrupt;

import org.bigcai.mpu.base.Interrupt;
import org.bigcai.mpu.base.InterruptController;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class ClockInterrupt extends Interrupt {
    // this clock interrupt is generated from timer that schedule at fixed rate.
    public Timer timer  = new Timer();

    private InterruptController interruptOwner;


    public ClockInterrupt(InterruptController interruptController, Integer interruptNumByInterruptOwner) {
        interruptOwner = interruptController;
        super.interruptNumByInterruptOwner = interruptNumByInterruptOwner;
    }

    public void runTimer(int period) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("当前时间: " + new Date());
                // timer to call owner of clock interrupt
                interruptOwner.callFrom(ClockInterrupt.super.getInterruptNumByInterruptOwner());
            }
        };

        // 每隔1000毫秒（1秒）执行一次
        timer.scheduleAtFixedRate(task, 0, period);
    }

}
