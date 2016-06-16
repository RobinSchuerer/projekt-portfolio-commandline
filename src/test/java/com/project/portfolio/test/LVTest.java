package com.project.portfolio.test;

import com.project.portfolio.ExcelManager;
import com.project.portfolio.domain.ProjectPortfolio;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class LVTest {

    XSSFWorkbook workbook = null;
    XSSFSheet sheet = null;
    ExcelManager excelManager = null;
    ProjectPortfolio portfolio = null;
    BufferedInputStream bufferedInputStream = null;

    @Before
    public void setUp() throws Exception {
        java.nio.file.Path resPath = java.nio.file.Paths.get(getClass().getResource("/012 Many-teams-many-projects Portfolio LV1871_1.xlsx").toURI());

        workbook = new XSSFWorkbook(Files.newInputStream(resPath));
        excelManager = new ExcelManager();
    }

    @After
    public void tearDown() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
    }

    @Test
    public void lvallteams() throws Exception {
        assertNotNull(workbook);


        portfolio = excelManager.readPortfolio(workbook);
        portfolio = excelManager.processProjectPortfolio(workbook);
    }
}
