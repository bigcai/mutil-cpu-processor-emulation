package org.bigcai.mpu;

import org.bigcai.mpu.base.BaseKernel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        BaseKernel myKernel = new MyKernel();
        ProcessorUnit processorUnit = new ProcessorUnit("[core 1]", myKernel);
        processorUnit.powerOn();
    }
}