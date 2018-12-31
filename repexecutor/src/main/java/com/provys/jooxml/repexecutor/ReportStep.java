package com.provys.jooxml.repexecutor;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Describes step in report execution.
 * Steps are immutable classes that form report definition and support report processing. Implementations of this
 * interface might retrieve data from dataset, copy cells from template to target worksheet and populate with data etc.
 * Report references its root step, steps generally form tree, even though substeps are kept private and not made
 * available through this interface
 */
public interface ReportStep {

    /**
     * @return internal name of given region, used as region identification, must be unique between children of given
     * parent
     */
    String getNameNm();

    /**
     * Add region processing to report pipeline
     *
     * @param pipeline ïs report processing pipeline
     * @return pipeline with added processing for this region
     */
    Stream<StepProcessor> addStepProcessing(Stream<StepProcessor> pipeline);

    /**
     * Get step processor supplier
     */
    Function<StepContext, StepProcessor> getProcessorSupplier();
}
