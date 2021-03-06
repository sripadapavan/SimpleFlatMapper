package org.sfm.csv.impl.writer;

import org.sfm.csv.CellWriter;
import org.sfm.reflect.Setter;

public class ObjectAppendableSetter implements Setter<Appendable, Object> {

    private final CellWriter cellWriter;

    public ObjectAppendableSetter(CellWriter cellWriter) {
        this.cellWriter = cellWriter;
    }

    @Override
    public void set(Appendable target, Object value) throws Exception {
        if (value != null) {
            cellWriter.writeValue(String.valueOf(value), target);
        }
    }
}
