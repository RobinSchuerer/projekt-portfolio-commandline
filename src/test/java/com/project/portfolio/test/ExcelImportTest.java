package com.project.portfolio.test;

import com.project.portfolio.ExcelManager;
import de.lv1871.projektportfolio.domain.ProjektPortfolio;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class ExcelImportTest {

    private ExcelManager excelManager = new ExcelManager();
    private XSSFWorkbook workbook;


    @Before
    public void setUp() throws Exception {
        Path resPath = Paths.get(getClass().getResource("/012 Many-teams-many-projects Portfolio LV1871_1.xlsx").toURI());

        workbook = new XSSFWorkbook(Files.newInputStream(resPath));
    }

    @After
    public void tearDown() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
    }

    @Test
    public void portfolioName() throws Exception {
        ProjektPortfolio portfolio = excelManager.read(workbook);
        assertEquals("LV1871-Many-teams-many-projects Portfolio 1",portfolio.getName());
    }
}
