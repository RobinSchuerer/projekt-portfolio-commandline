package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.reader.ProjektPortfolioExcelReader;
import de.lv1871.projektportfolio.test.TestUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServiceTestLv {

    private ProjektPortfolioVorschlagService service = ProjektPortfolioVorschlagService
            .newBuilder()
            .withPflichtProjektStrategy(new GleichverteilungMitFolgeCheck())
            .withStrategischeProjekteStrategy((pflichtProjekteVorschlag, eingabeDaten1, team1) -> pflichtProjekteVorschlag)
            .build();

    private ProjektPortfolioEingabeDaten eingabeDaten;
    private XSSFWorkbook workbook;

    @Before
    public void setUp() throws Exception {
        ProjektPortfolioExcelReader reader = new ProjektPortfolioExcelReader();
        String path = "/lv-projekte.xlsx";

        Path resPath = Paths.get(getClass().getResource(path).toURI());
        workbook = new XSSFWorkbook(Files.newInputStream(resPath));
        eingabeDaten = reader.read(workbook);
    }

    @Test
    public void testLVCase(){
        ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);
        ProjektPortfolioVorschlag erwartung = TestUtility.readTestErwartungen(workbook);

        TestUtility.vergleiche(erwartung,vorschlag);

    }

}
