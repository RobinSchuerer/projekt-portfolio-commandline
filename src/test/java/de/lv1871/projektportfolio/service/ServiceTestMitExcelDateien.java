package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.reader.ProjektPortfolioExcelReader;
import de.lv1871.projektportfolio.test.TestUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServiceTestMitExcelDateien {

    private ProjektPortfolioVorschlagService service = ProjektPortfolioVorschlagService
            .newBuilder()
            .withPflichtProjektStrategy(new GleichverteilungMitFolgeCheck())
            .withStrategischeProjekteStrategy(new SimpleStrategischeProjekteStrategy())
            .build();


    @Test
    public void test001() throws URISyntaxException, IOException {
        testeExcelDatei("/001 One-team-one-mhproject_v3.xlsx");
    }

    @Test
    public void test002() throws URISyntaxException, IOException {
        testeExcelDatei("/002 One-team-one-mhproject-with-overflow.xlsx");
    }

    @Test
    public void test003() throws URISyntaxException, IOException {
        testeExcelDatei("/003 One-team-one-pproject.xlsx");
    }

    @Test
    public void test003b() throws URISyntaxException, IOException {
        testeExcelDatei("/003b One-team-one-pproject-restriction-changed.xlsx");
    }

    @Test
    public void test004() throws URISyntaxException, IOException {
        testeExcelDatei("/004 One-team-one-pproject-with-overflow.xlsx");
    }

    @Test
    public void test005() throws URISyntaxException, IOException {
        testeExcelDatei("/005 One-team-two-mhprojects-with-overflow.xlsx");
    }

    @Test
    public void test006() throws URISyntaxException, IOException {
        testeExcelDatei("/006 Two-teams-one-mhproject-with-overflow.xlsx");
    }

    @Test
    public void test007() throws URISyntaxException, IOException {
        testeExcelDatei("/007 One-team-one-strategic-project.xlsx");
    }

    @Test
    public void test008() throws URISyntaxException, IOException {
        testeExcelDatei("/008 One-team-one-mh-one-strategic-project.xlsx");
    }

    @Test
    public void test009() throws URISyntaxException, IOException {
        testeExcelDatei("/009 Two-teams-two-mh-one-strategic-project_b.xlsx");
    }

    @Test
    public void test010() throws URISyntaxException, IOException {
        testeExcelDatei("/010 One-team-two-sprojects.xlsx");
    }

    @Test
    public void test013() throws URISyntaxException, IOException {
        testeExcelDatei("/013 Many-teams-many-projects_team1extracted.xlsx");
    }

    @Test
    public void test014() throws URISyntaxException, IOException {
        testeExcelDatei("/014 Many-teams-many-projects_team3extracted_v2.xlsx");
    }

    @Test
    public void test015() throws URISyntaxException, IOException {
        testeExcelDatei("/015 One-team-two-mh-one-prod-two-strategic-projects-with-overflow_v2.xlsx");
    }

    private void testeExcelDatei(String path) throws URISyntaxException, IOException {
        ProjektPortfolioExcelReader reader = new ProjektPortfolioExcelReader();


        Path resPath = Paths.get(getClass().getResource(path).toURI());
        XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(resPath));
        ProjektPortfolioEingabeDaten eingabeDaten = reader.read(workbook);

        ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);
        ProjektPortfolioVorschlag erwartung = TestUtility.readTestErwartungen(workbook);

        TestUtility.vergleiche(erwartung,vorschlag);
    }

}
