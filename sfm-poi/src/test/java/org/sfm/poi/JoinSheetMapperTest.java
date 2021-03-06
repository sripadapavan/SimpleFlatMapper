package org.sfm.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.sfm.test.jdbc.JoinTest;
import org.sfm.utils.ListCollectorHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//IFJAVA8_START
import java.util.stream.Collectors;
//IFJAVA8_END

public class JoinSheetMapperTest {

    Sheet joinSheet;

    SheetMapper<JoinTest.ProfessorGS> joinSheetMapper;

    @Before
    public void setUp(){
        Workbook wb = new HSSFWorkbook();
        joinSheet = wb.createSheet();


        for(int i = 0; i < JoinTest.ROWS.length; i++) {
            Object[] orow = JoinTest.ROWS[i];
            Row row = joinSheet.createRow(i);
            for(int j = 0; j < orow.length; j++) {
                Cell cell = row.createCell(j);
                if(orow[j] != null) {
                    if (orow[j] instanceof String) {
                        cell.setCellValue((String)orow[j]);
                    } else {
                        cell.setCellValue((Integer)orow[j]);
                    }
                }
            }
        }

        joinSheetMapper =
            SheetMapperFactory
                .newInstance()
                .newBuilder(JoinTest.ProfessorGS.class)
                .addKey("id")
                .addMapping("name")
                .addKey("students_id")
                .addMapping("students_name")
                .addMapping("students_phones_value")
                .mapper();

    }

    @Test
    public void iteratorOnSheetFrom0WithStaticMapper() {
        Iterator<JoinTest.ProfessorGS> iterator = joinSheetMapper.iterator(joinSheet);

        List<JoinTest.ProfessorGS> list = new ArrayList<JoinTest.ProfessorGS>();

        while(iterator.hasNext()) {
            list.add(iterator.next());
        }

        JoinTest.validateProfessors(list);
    }

    @Test
    public void forEachOnSheetFrom0WithStaticMapper() {
        JoinTest.validateProfessors(
                joinSheetMapper
                        .forEach(
                                joinSheet,
                                new ListCollectorHandler<JoinTest.ProfessorGS>()
                        ).getList()
        );

    }


    //IFJAVA8_START
    @Test
    public void streamOnSheetFrom0WithStreamWithStaticMapper() {

        List<JoinTest.ProfessorGS> list = joinSheetMapper.stream(joinSheet).collect(Collectors.toList());
        JoinTest.validateProfessors(list);

    }
    //IFJAVA8_END

}
