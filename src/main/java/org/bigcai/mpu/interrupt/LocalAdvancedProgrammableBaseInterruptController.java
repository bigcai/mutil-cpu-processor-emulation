package org.bigcai.mpu.interrupt;

import org.bigcai.mpu.ProcessorUnit;
import org.bigcai.mpu.base.BaseInterruptController;

public class LocalAdvancedProgrammableBaseInterruptController extends BaseInterruptController {

    /**
     * LocalAdvancedProgrammableInterruptController belong to ProcessorUnit, so need to bind to specified ProcessorUnit
     */
    private ProcessorUnit processorUnit;
    public LocalAdvancedProgrammableBaseInterruptController(ProcessorUnit processorUnit) {
        super();
        this.processorUnit = processorUnit;
    }

    @Override
    public void doCall(Integer interruptNum) {
        // send interrupt to processor unit , let interrupt checker of processor unit to do interrupt job.
        this.processorUnit.interruptStatus.put(interruptNum, true);
    }
}
