package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CellBindBuilderTest {

    private static CellCoordinates coordinates = mock(CellCoordinates.class);
    private static CellCoordinates otherCoordinates = mock(CellCoordinates.class);

    @Test
    void getSourceColumnTest() {
        var builder = new CellBindBuilder();
        assertThat(builder.getSourceColumn()).isEqualTo(Optional.empty());
        assertThat(builder.setSourceColumn("A_B")).isSameAs(builder);
        assertThat(builder.getSourceColumn()).isEqualTo(Optional.of("A_B"));
        assertThat(builder.setSourceColumn("c")).isSameAs(builder);
        assertThat(builder.getSourceColumn()).isEqualTo(Optional.of("C"));
        assertThat(builder.getCoordinates()).isEqualTo(Optional.empty());
    }

    @Test
    void getCoordinatesTest() {
        var builder = new CellBindBuilder();
        assertThat(builder.getCoordinates()).isEqualTo(Optional.empty());
        assertThat(builder.setCoordinates(coordinates)).isSameAs(builder);
        assertThat(builder.getCoordinates()).isEqualTo(Optional.of(coordinates));
        assertThat(builder.setCoordinates(otherCoordinates)).isSameAs(builder);
        assertThat(builder.getCoordinates()).isEqualTo(Optional.of(otherCoordinates));
        assertThat(builder.getSourceColumn()).isEqualTo(Optional.empty());
    }

    @Test
    void buildTest() {
        var builder = new CellBindBuilder().setSourceColumn("a_b_c").setCoordinates(coordinates);
        assertThat(builder.build()).isEqualTo(new CellBind("a_b_c", coordinates));
    }

    @Test
    void toStringTest() {
        when(coordinates.toString()).thenReturn("<coordinates>");
        var builder = new CellBindBuilder();
        assertThat(builder.toString()).isEqualTo("CellBindBuilder{sourceColumn=null, coordinates=null}");
        builder.setSourceColumn("a_b");
        assertThat(builder.toString()).isEqualTo("CellBindBuilder{sourceColumn='A_B', coordinates=null}");
        builder.setCoordinates(coordinates);
        assertThat(builder.toString()).isEqualTo("CellBindBuilder{sourceColumn='A_B', coordinates=<coordinates>}");
    }
}