package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class ExecRegionBase implements ExecRegion {

    private final String nameNm;

    ExecRegionBase(String nameNm) {
        this.nameNm = Objects.requireNonNull(nameNm);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return nameNm;
    }
}
