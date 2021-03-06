package org.sfm.csv.impl.cellreader.time;

import org.sfm.csv.CellValueReader;
import org.sfm.csv.ParsingContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JavaLocalTimeCellValueReader implements CellValueReader<LocalTime> {

    private final DateTimeFormatter formatter;

    public JavaLocalTimeCellValueReader(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalTime read(char[] chars, int offset, int length, ParsingContext parsingContext) {
        return LocalTime.parse(new String(chars, offset, length), formatter);
    }

}
