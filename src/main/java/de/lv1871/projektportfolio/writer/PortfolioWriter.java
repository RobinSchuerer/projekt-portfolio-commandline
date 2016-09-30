package de.lv1871.projektportfolio.writer;

import com.google.common.collect.Lists;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PortfolioWriter {


    public void write(XSSFWorkbook workbook, ProjektPortfolioVorschlag vorschlag) {
        writeOutput(workbook, vorschlag);
        writeProtokoll(workbook,vorschlag);
    }

    private void writeProtokoll(XSSFWorkbook workbook, ProjektPortfolioVorschlag vorschlag) {
        XSSFSheet sheet = workbook.createSheet("Protokoll");
        int zeilenIndex = 1;

        for (String eintrag : vorschlag.getProtokollEintrage()) {
            sheet.createRow(zeilenIndex++).createCell(0).setCellValue(eintrag);
        }

        sheet.createRow(zeilenIndex++).createCell(0).setCellValue("Verarbeitungsende" + LocalTime.now().toString());
    }

    private void writeOutput(XSSFWorkbook workbook, ProjektPortfolioVorschlag vorschlag) {
        XSSFSheet output = workbook.createSheet("Output");

        // monat header
        XSSFRow monatsZeile = output.createRow(0);
        monatsZeile.createCell(1).setCellValue("Monat");

        List<LocalDate> monate = Lists.newArrayList(vorschlag.getMonate());

        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("mmm-yy"));

        // alle Monate ausgeben
        for (int i = 2; i < monate.size() + 2; i++) {
            LocalDate monat = monate.get(i - 2);
            XSSFCell datumZelle = monatsZeile.createCell(i);
            datumZelle.setCellValue(from(monat));
            datumZelle.setCellStyle(dateStyle);
        }

        int zeilenIndex = 1;


        Set<String> teams = vorschlag.getTeams();
        for (String team : teams) {
            for (String projekt : vorschlag.getProjekte()) {
                // TODO: 31.08.16 gibt es diese kombination überhaupt
                Map<LocalDate, BigDecimal> aufwandProMonat = vorschlag.getMonatsAufwaende(team, projekt);

                if (aufwandProMonat.isEmpty()) {
                    continue;
                }

                XSSFRow zeile = output.createRow(zeilenIndex);
                zeile.createCell(0).setCellValue(team);
                zeile.createCell(1).setCellValue(projekt);

                for (int i = 2; i < monate.size() + 2; i++) {
                    LocalDate monat = monate.get(i - 2);
                    BigDecimal aufwand = aufwandProMonat.get(monat);
                    zeile.createCell(i).setCellValue(format(aufwand));
                }

                zeilenIndex++;
            }

        }

        // leerzeile
        zeilenIndex++;

        // overflow
        XSSFRow overflowTeam = output.createRow(zeilenIndex++);
        XSSFRow overflowValue = output.createRow(zeilenIndex++);
        overflowTeam.createCell(0).setCellValue("Overflow");

        int spalte = 1;
        for (String team : teams) {
            overflowTeam.createCell(spalte).setCellValue(team);
            overflowValue.createCell(spalte).setCellValue(format(vorschlag.getUeberlauf(team)));
            spalte++;
        }


        // deadline

        XSSFRow deadlinesProjekte = output.createRow(zeilenIndex++);
        XSSFRow deadlinesMonate = output.createRow(zeilenIndex++);

        deadlinesProjekte.createCell(0).setCellValue("Projekte");
        deadlinesMonate.createCell(0).setCellValue("deadline (last month)");

        int deadlineSpalte = 1;
        for (String projekt : vorschlag.getProjekte()) {
            Optional<LocalDate> deadline = vorschlag.getDeadline(projekt);
            if(!deadline.isPresent()){
                continue;
            }

            deadlinesProjekte.createCell(deadlineSpalte).setCellValue(projekt);
            XSSFCell deadlineMonatCell = deadlinesMonate.createCell(deadlineSpalte);
            deadlineMonatCell.setCellStyle(dateStyle);
            deadlineMonatCell.setCellValue(from(deadline.get()));

            deadlineSpalte++;
        }
    }

    @Nonnull
    private double format(@Nonnull Optional<BigDecimal> value) {
        if (!value.isPresent()) {
            return 0d;
        }

        return value.get().doubleValue();
    }

    @Nonnull
    private double format(@Nullable BigDecimal aufwand) {
        if (aufwand == null) {
            return 0d;
        }
        return aufwand.doubleValue();
    }

    @Nonnull
    private static Date from(@Nonnull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
