package com.baomidou.mybatisplus.generator.index;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper.Index;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
public class MapperMethodHandlerTest {

    @Test
    void shouldRecognizePrimaryKeyNames() {
        var method = new DefaultGenerateMapperMethodHandler();
        assertThat(method.isPrimaryKey("PRIMARY")).isTrue();
        assertThat(method.isPrimaryKey("primary")).isTrue();
        assertThat(method.isPrimaryKey("PRIMARY_KEY_1")).isTrue();
        assertThat(method.isPrimaryKey("primary_key_1")).isTrue();
        assertThat(method.isPrimaryKey("id_idx")).isFalse();
    }

    @Test
    void onlyUsesIndexesThatCoverTheWholeTable() throws SQLException {
        var table = mock(TableInfo.class);
        var indexes = List.of(index("uq_active_email", "email", "active"), index("uq_reference", "reference", null));
        when(table.getIndexList()).thenReturn(indexes);
        when(table.getTableFieldMap())
                .thenReturn(Map.of("email", mock(TableField.class), "reference", mock(TableField.class)));

        assertThat(new DefaultGenerateMapperMethodHandler().getMappedIndexes(table)).containsOnlyKeys("uq_reference");
        assertThat(new DefaultGenerateMapperLambdaMethodHandler().getMappedIndexes(table))
                .containsOnlyKeys("uq_reference");
    }

    private Index index(String name, String column, String condition) throws SQLException {
        var row = mock(ResultSet.class);
        when(row.getString("INDEX_NAME")).thenReturn(name);
        when(row.getString("COLUMN_NAME")).thenReturn(column);
        when(row.getString("FILTER_CONDITION")).thenReturn(condition);
        return new Index(row);
    }

}
