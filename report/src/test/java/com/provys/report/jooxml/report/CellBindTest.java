package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CellBindTest {

    @Mock
    static CellCoordinates coordinates = mock(CellCoordinates.class);

    @Nonnull
    static Stream<Object[]> getNewTest() {
        return Stream.of(
                new Object[]{"abc", coordinates, null}
                , new Object[]{"X_Y1", coordinates, null}
                , new Object[]{"a.b", coordinates, IllegalArgumentException.class}
                , new Object[]{"1a", coordinates, IllegalArgumentException.class}
                , new Object[]{null, coordinates, NullPointerException.class}
                , new Object[]{"abc", null, NullPointerException.class}
        );
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @MethodSource
    void getNewTest(@Nullable String sourceColumn, @Nullable CellCoordinates coordinates, @Nullable Class<Throwable> ex)
    {
        if (ex == null) {
            assertThatCode(() -> new CellBind(sourceColumn, coordinates)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> new CellBind(sourceColumn, coordinates)).isInstanceOf(ex);
        }
    }

    @Test
    void getSourceColumnTest() {
        assertThat(new CellBind("a_b", coordinates).getSourceColumn()).
                isEqualTo("A_B");
    }

    @Test
    void getCoordinatesTest() {
        assertThat(new CellBind("a_b", coordinates).getCoordinates()).
                isSameAs(coordinates);
    }

    @Test
    void toStringTest() {
        when(coordinates.toString()).thenReturn("<coordinates>");
        assertThat(new CellBind("A_B", coordinates).toString()).
                isEqualTo("CellBind{sourceColumn='A_B', coordinates=<coordinates>}");
    }
}