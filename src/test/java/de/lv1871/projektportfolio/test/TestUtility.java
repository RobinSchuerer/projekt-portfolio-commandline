package de.lv1871.projektportfolio.test;

import com.google.common.base.Preconditions;
import de.lv1871.projektportfolio.domain.Projekt;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;


public class TestUtility {

    @Nonnull
	public static ProjektPortfolioVorschlag readTestErwartungen(@Nonnull final XSSFWorkbook workbook){
        Preconditions.checkNotNull(workbook);

        XSSFSheet sheet = workbook.getSheetAt(0);

        // suche nach String "Output"
        Optional<Integer> indexOutput = getZeilenIndexOutput(sheet);
        if (!indexOutput.isPresent()) {
            throw new RuntimeException("kein 'OUTPUT' in der ersten Spalte gefunden!");
        }

        Integer indexOutputZeile = indexOutput.get();

        // Zeilen Index Projekte
        int indexProjektZeile = indexOutputZeile + 1;

        // Zeilen Index Teams
        int indexTeamZeile = indexOutputZeile + 2;

        ProjektPortfolioVorschlag vorschlag = ProjektPortfolioVorschlag.newBuilder().build();

        // beginne das Lesen der Werte
        for (int i = indexOutputZeile +3; i < Integer.MAX_VALUE; i++) {
            XSSFRow zeile = sheet.getRow(i);
            if(zeile == null){
                break;
            }

            XSSFCell datumsZelle = zeile.getCell(0);
            if(datumsZelle == null ){
                break;
            }

            Date datum = datumsZelle.getDateCellValue();
            LocalDate monat = Instant.ofEpochMilli(datum.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

            for (int j = 1; j<Integer.MAX_VALUE; j++){
                XSSFCell zelle = zeile.getCell(j);
                if(zelle == null || zelle.getCellType() != Cell.CELL_TYPE_NUMERIC){
                    break;
                }

                double value = zelle.getNumericCellValue();
                BigDecimal aufwand = new BigDecimal(value);

                String team = sheet
                        .getRow(indexTeamZeile)
                        .getCell(zelle.getColumnIndex())
                        .getStringCellValue();
                String projekt = sheet
                        .getRow(indexProjektZeile)
                        .getCell(zelle.getColumnIndex())
                        .getStringCellValue();

                vorschlag.add(
                        Team.newBuilder().withName(team).build(),
                        Projekt.newBuilder().withName(projekt).build(),
                        monat,
                        aufwand
                        );
            }
        }
        return vorschlag;
	}

	@Nonnull
    private Optional<Integer> getZeilenIndexOutput(@Nonnull XSSFSheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        while(rowIterator.hasNext()){
            Row zeile = rowIterator.next();

            Cell zelle = zeile.getCell(0);
            if (zelle == null
                    || zelle.getCellType() != Cell.CELL_TYPE_STRING
                    || zelle.getStringCellValue().equals("OUTPUT")) {
                continue;
            }

            return Optional.of(zelle.getRowIndex());
        }

        return Optional.empty();
    }


    public static void vergleiche(@Nonnull ProjektPortfolioVorschlag erwartung,
                                  @Nonnull ProjektPortfolioVorschlag vorschlag) {
    }
}
