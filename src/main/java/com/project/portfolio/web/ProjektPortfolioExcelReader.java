package com.project.portfolio.web;

import de.lv1871.projektportfolio.domain.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * Created by SchuererR on 12.07.2016.
 */
public class ProjektPortfolioExcelReader {

    public ProjektPortfolio read(XSSFWorkbook workbook) throws Exception {
        XSSFSheet sheet = workbook.getSheetAt(0);

        String portfolioName = getPortfolioName(sheet);

        return ProjektPortfolio
                .newBuilder()
                .withName(portfolioName)
                .withTeamKapazitaeten(readTeamKapazitaeten(sheet))
                .withBeschraenkungen(readBeschraenkungen(sheet))
                .withProjektAufwaende(readProjektAufwaende(sheet))
                .build();

    }

    private List<ProjektAufwand> readProjektAufwaende(XSSFSheet sheet) {

        // projekte bestimmen und in map mit spalten index vorhalten
        Map<Projekt, Integer> projekte = getProjektMap(sheet);

        // beteiligte teams bestimmen und in map mit zeilen index vorhalten
        Map<Team, Integer> teams = getTeamMap(sheet);

        // transformation in projekte und die aufwände für dieses projekt
        return projekte.entrySet().stream().map(p -> {
            Projekt projekt = p.getKey();

            Map<Team, BigDecimal> aufwaende = teams.entrySet().stream().collect(Collectors.toMap(t -> t.getKey(), t -> {
                double aufwand = sheet.getRow(t.getValue()).getCell(p.getValue()).getNumericCellValue();
                return new BigDecimal(aufwand);
            }));

            return ProjektAufwand.newBuilder().withProjekt(projekt).withAufwaende(aufwaende).build();

        }).collect(Collectors.toList());
    }

    private Map<Team, Integer> getTeamMap(XSSFSheet sheet) {
        int indexEffortZeile = getIndexOfZeileStartingWithValue(sheet, "effort");


        Map<Team, Integer> result = new HashMap<>();

        for (int i = indexEffortZeile; i < Integer.MAX_VALUE; i++) {
            XSSFRow teamZeile = sheet.getRow(i);
            XSSFCell teamCell = teamZeile.getCell(i);
            if(teamCell == null || teamCell.getCellType() != CELL_TYPE_STRING){
                break;
            }

            Team team = Team
                    .newBuilder()
                    .withName(teamCell.getStringCellValue())
                    .build();
            result.put(team, i);
        }

        return result;
    }

    private Map<Projekt, Integer> getProjektMap(XSSFSheet sheet) {

        Map<Projekt, Integer> result = new HashMap<>();
        int indexProjektZeile = getIndexOfZeileStartingWithValue(sheet, "projects");

        XSSFRow projektZeile = sheet.getRow(indexProjektZeile);
        XSSFRow projektTypZeile = sheet.getRow(indexProjektZeile+1);
        XSSFRow prioZeile = sheet.getRow(indexProjektZeile+2);
        XSSFRow deadlineZeile = sheet.getRow(indexProjektZeile+3);

        for (int i = 2; i < Integer.MAX_VALUE ; i++) {
            XSSFCell projektNameCell = projektZeile.getCell(i);
            if(projektNameCell == null || projektNameCell.getCellType() == CELL_TYPE_BLANK){
                break;
            }

            String typValue = projektTypZeile.getCell(i).getStringCellValue();
            double prioValue = prioZeile.getCell(i).getNumericCellValue();

            XSSFCell deadlineCell = deadlineZeile.getCell(i);

            Projekt projekt = Projekt
                    .newBuilder()
                    .withName(projektNameCell.getStringCellValue())
                    .withPrioritaet(new BigDecimal(prioValue).intValue())
                    .withDeadLine( getDeadLine(deadlineCell))
                    .withTyp(ProjektTyp.parse(typValue))
                    .build();
            result.put(projekt,i);
        }

        return result;
    }

    private LocalDate getDeadLine(XSSFCell deadlineCell) {
        if(deadlineCell.getCellType() != CELL_TYPE_NUMERIC ){
            return null;
        }

        Date deadlineValue = deadlineCell.getDateCellValue();
        return from(deadlineValue);
    }

    private List<Beschraenkung> readBeschraenkungen(XSSFSheet sheet) {
        List<Beschraenkung> result = new ArrayList<>();

        Iterator<Row> iterator = getRowIteratorAbZeileInput(sheet);
        int index = getIndexNaechsterInhalt(iterator);
        for (int i = index; i < index + 99; i++) {
            Row row = sheet.getRow(i);


            Cell cell1 = row.getCell(0);
            Cell cell2 = row.getCell(1);

            if (cell1 == null || cell1.getCellType() == CELL_TYPE_BLANK) {
                break;
            }

            if (cell1.getCellType() == CELL_TYPE_STRING && cell1.getStringCellValue().startsWith("Restriction")) {
                Beschraenkung beschraenkung = createBeschraenkung(cell1.getStringCellValue(), cell2.getNumericCellValue());
                result.add(beschraenkung);
            }
        }

        return result;
    }

    public List<TeamKapazitaet> readTeamKapazitaeten(XSSFSheet sheet) {
        List<TeamKapazitaet> result = new ArrayList<>();

        Map<LocalDate, Integer> dateIndexMap = getDateIndexMap(sheet);
        Map<Team, Integer> teamSpaltenIndexMap = getTeamSpaltenIndexMap(sheet);

        for (Map.Entry<LocalDate, Integer> dateEntry : dateIndexMap.entrySet()) {

            for (Map.Entry<Team, Integer> teamEntry : teamSpaltenIndexMap.entrySet()) {

                TeamKapazitaet teamKapazitaet = TeamKapazitaet
                        .newBuilder()
                        .withKapazitaet(getValue(sheet, dateEntry.getValue(), teamEntry.getValue()))
                        .withTeam(teamEntry.getKey())
                        .withMonat(dateEntry.getKey())
                        .build();

                result.add(teamKapazitaet);
            }

        }

        return result;
    }

    private BigDecimal getValue(XSSFSheet sheet, int row, int column) {
        return new BigDecimal(sheet.getRow(row).getCell(column).getNumericCellValue());
    }

    private Map<LocalDate, Integer> getDateIndexMap(XSSFSheet sheet) {
        Map<LocalDate, Integer> result = new HashMap<>();

        int teamZeileIndex = getIndexOfZeileStartingWithValue(sheet, "month");

        for (int i = teamZeileIndex + 1; i < teamZeileIndex + 50; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            XSSFCell monthCell = row.getCell(0);
            if (monthCell.getCellType() == CELL_TYPE_BLANK) {
                break;
            }

            Date dateCellValue = monthCell.getDateCellValue();

            result.put(from(dateCellValue), i);


        }

        return result;
    }

    private static LocalDate from(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Map<Team, Integer> getTeamSpaltenIndexMap(XSSFSheet sheet) {
        Map<Team, Integer> result = new HashMap<>();

        int teamZeileIndex = getIndexOfZeileStartingWithValue(sheet, "teams");
        Iterator<Cell> cellIterator = sheet.getRow(teamZeileIndex).cellIterator();

        // in der ersten Zelle steht 'teams'
        cellIterator.next();

        while (cellIterator.hasNext()) {
            Cell teamCell = cellIterator.next();
            result.put(Team.newBuilder().withName(teamCell.getStringCellValue()).build(), teamCell.getColumnIndex());
        }

        return result;
    }

    private Beschraenkung createBeschraenkung(String stringCellValue, double numericCellValue) {
        String typValue = stringCellValue.substring(12);
        BigDecimal beschraenkungValue = new BigDecimal(numericCellValue);

        return Beschraenkung
                .newBuilder()
                .withTyp(ProjektTyp.parse(typValue))
                .withValue(beschraenkungValue)
                .build();

    }

    private int getIndexNaechsterInhalt(Iterator<Row> rowIterator) {
        int rowNum = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(0);
            if (cell.getCellType() == CELL_TYPE_BLANK) {
                continue;
            }
            return row.getRowNum();

        }

        throw new RuntimeException();
    }

    private Iterator<Row> getRowIteratorAbZeileInput(XSSFSheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CELL_TYPE_STRING && "INPUT".equals(cell.getStringCellValue())) {
                    return rowIterator;
                }
            }
        }

        throw new RuntimeException("kein Input Block gefunden");
    }

    private int getIndexOfZeileStartingWithValue(XSSFSheet sheet, String value) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CELL_TYPE_STRING && value.equals(cell.getStringCellValue())) {
                    return row.getRowNum();
                }
            }
        }

        throw new RuntimeException("nicht gefunden");
    }

    private String getPortfolioName(XSSFSheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row zeile = rowIterator.next();
            // erste Zelle der Zeile soll Portfolio sein
            Cell ersteZelle = zeile.getCell(0);
            if (ersteZelle.getCellType() == Cell.CELL_TYPE_STRING && "portfolio".equals(ersteZelle.getStringCellValue())) {
                return zeile.getCell(1).getStringCellValue();
            }
        }

        throw new AssertionError("Portfolio Name konnte nicht gefunden werden");
    }
}
