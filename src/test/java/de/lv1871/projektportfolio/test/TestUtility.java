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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestUtility {

    @Nonnull
    public static ProjektPortfolioVorschlag readTestErwartungen(@Nonnull final XSSFWorkbook workbook) {
        Preconditions.checkNotNull(workbook);

        XSSFSheet sheet = workbook.getSheetAt(0);

        ProjektPortfolioVorschlag vorschlag = readMonatsWerte(sheet);
        readUeberlauf(sheet, vorschlag);
        readDeadLine(sheet,vorschlag);

        return vorschlag;
    }

    private static void readDeadLine(@Nonnull XSSFSheet sheet, @Nonnull ProjektPortfolioVorschlag vorschlag) {
        Optional<Integer> zeilenIndex = getZeilenIndexDeadline(sheet);
        if(!zeilenIndex.isPresent()){
            throw new RuntimeException("Keine Deadline gefunden");
        }

        Integer indexDeadlineZeile = zeilenIndex.get();
        XSSFRow deadlineZeile = sheet.getRow(indexDeadlineZeile);

        int indexProjekt = indexDeadlineZeile - 1;
        XSSFRow projektZeile = sheet.getRow(indexProjekt);
        for (int i = 2; i < Integer.MAX_VALUE; i++){
            XSSFCell projektZelle = projektZeile.getCell(i);
            if(projektZelle == null){
                break;
            }

            String projektName = projektZelle.getStringCellValue();

            XSSFCell deadlineZelle = deadlineZeile.getCell(i);

            Date datum = deadlineZelle.getDateCellValue();

            vorschlag.addDeadLine(projektName,from(datum));
        }
    }

    @Nonnull
    private static Optional<Integer> getZeilenIndexDeadline(@Nonnull XSSFSheet sheet) {

        Optional<Integer> zeilenIndexOutput = getZeilenIndex(sheet, "OUTPUT");
        if(!zeilenIndexOutput.isPresent()){
            throw new RuntimeException("Keine Deadline gefunden");
        }

        for(int i = zeilenIndexOutput.get(); i < 250; i++){
            XSSFRow zeile = sheet.getRow(i);
            if(zeile == null){
                continue;
            }

            XSSFCell ersteZelle = zeile.getCell(0);
            if(ersteZelle == null || ersteZelle.getCellType() != Cell.CELL_TYPE_STRING){
                continue;
            }

            if(ersteZelle.getStringCellValue().equals("deadline (last month)")){
                return Optional.of(zeile.getRowNum());
            }

            continue;

        }

        return Optional.empty();
    }

    private static ProjektPortfolioVorschlag readMonatsWerte(XSSFSheet sheet) {
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
        for (int i = indexOutputZeile + 3; i < Integer.MAX_VALUE; i++) {
            XSSFRow zeile = sheet.getRow(i);
            if (zeile == null) {
                break;
            }

            XSSFCell datumsZelle = zeile.getCell(0);
            if (datumsZelle == null || datumsZelle.getDateCellValue() == null) {
                break;
            }

            Date datum = datumsZelle.getDateCellValue();

            LocalDate monat = from(datum);

            for (int j = 1; j < Integer.MAX_VALUE; j++) {
                XSSFCell zelle = zeile.getCell(j);
                if (zelle == null || zelle.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                    break;
                }

                double value = zelle.getNumericCellValue();
                BigDecimal aufwand = new BigDecimal(value);

                int spaltenIndex = zelle.getColumnIndex();
                String team = sheet
                        .getRow(indexTeamZeile)
                        .getCell(spaltenIndex)
                        .getStringCellValue();
                XSSFCell teamCell = sheet
                        .getRow(indexProjektZeile)
                        .getCell(spaltenIndex);

                Preconditions.checkNotNull(teamCell,
                        "Konnte keine Zelle für Team finden: " + indexProjektZeile + "," + spaltenIndex);
                Preconditions.checkArgument(teamCell.getCellType() == Cell.CELL_TYPE_STRING,
                        "Konnte keine Zelle für Team finden: " + indexProjektZeile + "," + spaltenIndex);

                String projekt = teamCell.getStringCellValue();

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

    private static LocalDate from(Date datum) {
        return Instant.ofEpochMilli(datum.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static void readUeberlauf(XSSFSheet sheet, ProjektPortfolioVorschlag vorschlag) {
        Optional<Integer> ueberlauf = getZeilenIndexOverflow(sheet);
        if (!ueberlauf.isPresent()) {
            throw new RuntimeException("kein 'Overflow' in der ersten Spalte gefunden!");
        }

        Integer indexOverflowZeile = ueberlauf.get();
        XSSFRow overflowTeamZeile = sheet.getRow(indexOverflowZeile);
        if (overflowTeamZeile == null) {
            throw new RuntimeException("Kein Overflow gefunden");
        }

        XSSFRow overflowValuesZeile = sheet.getRow(indexOverflowZeile + 1);
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            XSSFCell teamCell = overflowTeamZeile.getCell(i);
            if (teamCell == null) {
                break;
            }
            String teamName = teamCell.getStringCellValue();


            XSSFCell valueCell = overflowValuesZeile.getCell(i);
            if (valueCell == null) {
                throw new RuntimeException("Overflow: Kein Wert für " + teamName);
            }

            vorschlag.addUeberlauf(teamName, new BigDecimal(valueCell.getNumericCellValue()));

        }
    }

    @Nonnull
    private static Optional<Integer> getZeilenIndexOutput(@Nonnull XSSFSheet sheet) {
        return getZeilenIndex(sheet, "OUTPUT");
    }

    @Nonnull
    private static Optional<Integer> getZeilenIndexOverflow(@Nonnull XSSFSheet sheet) {
        return getZeilenIndex(sheet, "Overflow");
    }

    @Nonnull
    private static Optional<Integer> getZeilenIndex(@Nonnull XSSFSheet sheet, @Nonnull String suchbegriff) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext()) {
            Row zeile = rowIterator.next();

            Cell zelle = zeile.getCell(0);
            if (zelle == null
                    || zelle.getCellType() != Cell.CELL_TYPE_STRING) {
                continue;
            }

            if (zelle.getStringCellValue().equals(suchbegriff)) {
                return Optional.of(zelle.getRowIndex());
            }
        }

        return Optional.empty();
    }


    public static void vergleiche(@Nonnull ProjektPortfolioVorschlag erwartung,
                                  @Nonnull ProjektPortfolioVorschlag vorschlag) {

        assertAll("VorschlagVergleich", () -> {

            vorschlag.getAufwandVerteilungen().forEach(aufwaendeProTeamUndProjekt -> {

                Team team = aufwaendeProTeamUndProjekt.getTeam();
                Projekt projekt = aufwaendeProTeamUndProjekt.getProjekt();

                aufwaendeProTeamUndProjekt.getAufwaende().forEach(aufwandProMonat -> {

                    LocalDate monat = aufwandProMonat.getMonat();
                    BigDecimal aufwand = aufwandProMonat.getAufwand();

                    Optional<BigDecimal> aufwandOptional = erwartung.getAufwand(team.getName(), projekt.getName(), monat);
                    assertTrue(aufwandOptional.isPresent(), getMessage(team, projekt, monat));

                    BigDecimal erwartungswert = aufwandOptional.get();
                    assertEquals(erwartungswert, aufwand, getMessage(team, projekt, monat));
                });

                vorschlag.getTeams().forEach(s -> {
                    assertEquals(erwartung.getUeberlauf(s), vorschlag.getUeberlauf(s), "Overflow: " + s);
                });

                vorschlag.getProjekte().forEach(projektName -> {
                            assertEquals(
                                    erwartung.getDeadline(projektName),
                                    vorschlag.getDeadline(projektName),
                                    "Deadline: " + projektName);
                        }
                );
            });
        });
    }

    private static String getMessage(Team team, Projekt projekt, LocalDate monat) {
        return "Team: " + team.getName() + " Projekt: " + projekt.getName() + " Monat: " + monat.format(DateTimeFormatter.ofPattern("MMM YY"));
    }
}
