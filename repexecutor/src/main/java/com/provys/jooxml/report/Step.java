package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportStep;

abstract class Step implements ReportStep {
    private final String nameNm;

    Step(String nameNm) {
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name of step cannot be empty");
        }
        this.nameNm = nameNm;
    }

    @Override
    public String getNameNm() {
        return nameNm;
    }
}
