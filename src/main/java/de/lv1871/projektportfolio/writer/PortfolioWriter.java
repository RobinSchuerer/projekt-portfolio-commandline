package de.lv1871.projektportfolio.writer;

import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Created by robin on 31.08.16.
 */
@Service
public class PortfolioWriter {

    public void write(XSSFWorkbook workbook, ProjektPortfolioVorschlag vorschlag){
        XSSFSheet zweitesSheet = workbook.createSheet("Output");


        zweitesSheet.createRow(0).createCell(0).setCellValue("test");



    }

}
