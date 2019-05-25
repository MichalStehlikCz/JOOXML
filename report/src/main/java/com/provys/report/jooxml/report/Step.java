package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;

import javax.annotation.Nonnull;

abstract class Step implements ReportStep {

    @Nonnull
    private final String nameNm;

    Step(String nameNm) {
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name of step cannot be empty");
        }
        this.nameNm = nameNm;
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return nameNm;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "nameNm='" + nameNm + '\'' +
                '}';
    }
}
