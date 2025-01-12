package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        BaseKernel myKernel = new MyKernel();


        Thread core1 = new Thread(() -> {
            ProcessorUnit processorUnit1 = new ProcessorUnit("[core 1]", myKernel, 10);
            processorUnit1.powerOn();
        });
        core1.start();

        Thread core2 = new Thread(() -> {
            ProcessorUnit processorUnit2 = new ProcessorUnit("[core 2]", myKernel,20);
            processorUnit2.powerOn();
        });
        core2.start();


        Thread core3 = new Thread(() -> {
            ProcessorUnit processorUnit3 = new ProcessorUnit("[core 3]", myKernel,20);
            processorUnit3.powerOn();
        });
        core3.start();


        Thread core4 = new Thread(() -> {
            ProcessorUnit processorUnit4 = new ProcessorUnit("[core 4]", myKernel,20);
            processorUnit4.powerOn();
        });
        core4.start();
    }
}