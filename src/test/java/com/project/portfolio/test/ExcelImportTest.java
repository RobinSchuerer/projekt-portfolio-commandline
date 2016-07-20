package com.project.portfolio.test;

import com.project.portfolio.web.ProjektPortfolioExcelReader;
import de.lv1871.projektportfolio.domain.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class ExcelImportTest {

    private ProjektPortfolioExcelReader reader = new ProjektPortfolioExcelReader();
    private XSSFWorkbook workbook;
    private ProjektPortfolio portfolio;

    @Before
    public void setUp() throws Exception {
        Path resPath = Paths.get(getClass().getResource("/lv-projekte.xlsx").toURI());

        workbook = new XSSFWorkbook(Files.newInputStream(resPath));
        portfolio = reader.read(workbook);
    }

    @After
    public void tearDown() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
    }

    @Test
    public void portfolioName() throws Exception {
        assertEquals("LV1871-Many-teams-many-projects Portfolio 1", portfolio.getName());
    }

    @Test
    public void beschraenkungen() throws Exception {
        List<Beschraenkung> beschraenkungen = portfolio.getBeschraenkungen();
        assertEquals(2, beschraenkungen.size());

        Beschraenkung beschraenkung1 = beschraenkungen.get(0);
        assertEquals(ProjektTyp.MUSS_PROJEKT, beschraenkung1.getTyp());
        assertEquals(new BigDecimal("0.80"), beschraenkung1.getValue());

        Beschraenkung beschraenkung2 = beschraenkungen.get(1);
        assertEquals(ProjektTyp.PRODUKT_PROJEKT, beschraenkung2.getTyp());
        assertEquals(new BigDecimal("1.00"), beschraenkung2.getValue());
    }

    @Test
    public void teams() throws Exception {
        Set<Team> teams = portfolio.getTeams();

        assertEquals(5, teams.size());

        assertTrue(teams.contains(Team.newBuilder().withName("SAP").build()));
        assertTrue(teams.contains(Team.newBuilder().withName("DMS").build()));
        assertTrue(teams.contains(Team.newBuilder().withName("Druck").build()));
        assertTrue(teams.contains(Team.newBuilder().withName("VVS").build()));
        assertTrue(teams.contains(Team.newBuilder().withName("CZWEI").build()));
    }

    @Test
    public void kapazitaet() throws Exception {
        List<String> result = portfolio
                .getTeamKapazitaeten()
                .stream()
                .map(tk -> tk.toString())
                .collect(Collectors.toList());

        List<String> expectations = Arrays.asList(
                "CZWEI: Mär 16 - 74.10",
                "CZWEI: Apr 16 - 54.20",
                "CZWEI: Mai 16 - 48.80",
                "CZWEI: Jun 16 - 62.60",
                "CZWEI: Jul 16 - 62.40",
                "CZWEI: Aug 16 - 55.50",
                "CZWEI: Sep 16 - 54.40",
                "CZWEI: Okt 16 - 76.00",
                "CZWEI: Nov 16 - 84.40",
                "CZWEI: Dez 16 - 68.30",
                "CZWEI: Jan 16 - 64.07",
                "CZWEI: Feb 17 - 64.07",
                "CZWEI: Mär 17 - 64.07",
                "CZWEI: Apr 17 - 64.07",
                "CZWEI: Mai 17 - 64.07",
                "CZWEI: Jun 17 - 64.07",
                "CZWEI: Jul 17 - 64.07",
                "CZWEI: Aug 17 - 64.07",
                "CZWEI: Sep 17 - 64.07",
                "CZWEI: Okt 17 - 64.07",
                "CZWEI: Nov 17 - 64.07",
                "CZWEI: Dez 17 - 64.07",
                "CZWEI: Jan 18 - 64.07",
                "CZWEI: Feb 18 - 64.07",
                "DMS: Mär 16 - 27.80",
                "DMS: Apr 16 - 27.80",
                "DMS: Mai 16 - 16.10",
                "DMS: Jun 16 - 29.20",
                "DMS: Jul 16 - 30.50",
                "DMS: Aug 16 - 23.70",
                "DMS: Sep 16 - 20.50",
                "DMS: Okt 16 - 25.20",
                "DMS: Nov 16 - 29.40",
                "DMS: Dez 16 - 25.20",
                "DMS: Jan 16 - 25.54",
                "DMS: Feb 17 - 25.54",
                "DMS: Mär 17 - 25.54",
                "DMS: Apr 17 - 25.54",
                "DMS: Mai 17 - 25.54",
                "DMS: Jun 17 - 25.54",
                "DMS: Jul 17 - 25.54",
                "DMS: Aug 17 - 25.54",
                "DMS: Sep 17 - 25.54",
                "DMS: Okt 17 - 25.54",
                "DMS: Nov 17 - 25.54",
                "DMS: Dez 17 - 25.54",
                "DMS: Jan 18 - 25.54",
                "DMS: Feb 18 - 25.54",
                "Druck: Mär 16 - 9.80",
                "Druck: Apr 16 - 11.00",
                "Druck: Mai 16 - 11.60",
                "Druck: Jun 16 - 0.00",
                "Druck: Jul 16 - 14.00",
                "Druck: Aug 16 - 16.90",
                "Druck: Sep 16 - 12.30",
                "Druck: Okt 16 - 13.20",
                "Druck: Nov 16 - 16.50",
                "Druck: Dez 16 - 13.20",
                "Druck: Jan 16 - 11.85",
                "Druck: Feb 17 - 11.85",
                "Druck: Mär 17 - 11.85",
                "Druck: Apr 17 - 11.85",
                "Druck: Mai 17 - 11.85",
                "Druck: Jun 17 - 11.85",
                "Druck: Jul 17 - 11.85",
                "Druck: Aug 17 - 11.85",
                "Druck: Sep 17 - 11.85",
                "Druck: Okt 17 - 11.85",
                "Druck: Nov 17 - 11.85",
                "Druck: Dez 17 - 11.85",
                "Druck: Jan 18 - 11.85",
                "Druck: Feb 18 - 11.85",
                "SAP: Mär 16 - 31.70",
                "SAP: Apr 16 - 31.20",
                "SAP: Mai 16 - 28.40",
                "SAP: Jun 16 - 49.90",
                "SAP: Jul 16 - 49.00",
                "SAP: Aug 16 - 41.50",
                "SAP: Sep 16 - 39.60",
                "SAP: Okt 16 - 40.80",
                "SAP: Nov 16 - 47.90",
                "SAP: Dez 16 - 44.10",
                "SAP: Jan 16 - 40.41",
                "SAP: Feb 17 - 40.41",
                "SAP: Mär 17 - 40.41",
                "SAP: Apr 17 - 40.41",
                "SAP: Mai 17 - 40.41",
                "SAP: Jun 17 - 40.41",
                "SAP: Jul 17 - 40.41",
                "SAP: Aug 17 - 40.41",
                "SAP: Sep 17 - 40.41",
                "SAP: Okt 17 - 40.41",
                "SAP: Nov 17 - 40.41",
                "SAP: Dez 17 - 40.41",
                "SAP: Jan 18 - 40.41",
                "SAP: Feb 18 - 40.41",
                "VVS: Mär 16 - 31.70",
                "VVS: Apr 16 - 31.20",
                "VVS: Mai 16 - 28.40",
                "VVS: Jun 16 - 49.90",
                "VVS: Jul 16 - 49.00",
                "VVS: Aug 16 - 41.50",
                "VVS: Sep 16 - 39.60",
                "VVS: Okt 16 - 40.80",
                "VVS: Nov 16 - 47.90",
                "VVS: Dez 16 - 44.10",
                "VVS: Jan 16 - 40.41",
                "VVS: Feb 17 - 40.41",
                "VVS: Mär 17 - 40.41",
                "VVS: Apr 17 - 40.41",
                "VVS: Mai 17 - 40.41",
                "VVS: Jun 17 - 40.41",
                "VVS: Jul 17 - 40.41",
                "VVS: Aug 17 - 40.41",
                "VVS: Sep 17 - 40.41",
                "VVS: Okt 17 - 40.41",
                "VVS: Nov 17 - 40.41",
                "VVS: Dez 17 - 40.41",
                "VVS: Jan 18 - 40.41",
                "VVS: Feb 18 - 40.41"
        );

        assertEquals(expectations.size(),result.size());

        expectations.forEach(e->assertTrue(result.contains(e)));
    }

    @Test
    public void basisRiester() throws Exception {
        Optional<ProjektAufwand> projektAufwand = getProjektAufwand("BasisRiester");

        assertTrue(projektAufwand.isPresent());


    }

    private Optional<ProjektAufwand> getProjektAufwand(final String projektName) {
        return portfolio.getProjektAufwaende().stream().filter(pa -> projektName.equals(pa.getProjekt().getName())).findFirst();
    }
}