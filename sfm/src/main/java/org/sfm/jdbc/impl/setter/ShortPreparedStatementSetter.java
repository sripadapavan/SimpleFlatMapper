package org.sfm.jdbc.impl.setter;

import org.sfm.reflect.Setter;
import org.sfm.reflect.primitive.ShortSetter;

import java.sql.PreparedStatement;

public class ShortPreparedStatementSetter implements Setter<PreparedStatement, Short>, ShortSetter<PreparedStatement> {

    private final int columnIndex;
    private final ShortPreparedStatementIndexSetter setter = new ShortPreparedStatementIndexSetter();

    public ShortPreparedStatementSetter(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public void set(PreparedStatement target, Short value) throws Exception {
        setter.set(target, value, columnIndex);
    }

    @Override
    public void setShort(PreparedStatement target, short value) throws Exception {
        setter.setShort(target, value, columnIndex);
    }
}
