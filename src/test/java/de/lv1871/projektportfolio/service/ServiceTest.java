package de.lv1871.projektportfolio.service;

import com.google.common.collect.Lists;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;
import de.lv1871.projektportfolio.domain.TeamKapazitaet;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ServiceTest {

    private ProjektPortfolioVorschlagService service = new ProjektPortfolioVorschlagService();

    @Test
    public void base() throws Exception {
        ProjektPortfolioEingabeDaten test = ProjektPortfolioEingabeDaten
                .newBuilder()
                .withTeamKapazitaeten(
                        Lists.newArrayList(
                                TeamKapazitaet
                                        .newBuilder()
                                        .withKapazitaet(BigDecimal.TEN)
                                        .withMonat(LocalDate.parse("2016-01-01"))
                                        .withTeam(Team.newBuilder().withName("test").build())
                                        .build()
                        ))
                .build();

        ProjektPortfolioVorschlag vorschlag = service.berechne(test);

        assertEquals(1, vorschlag.getAufwandVerteilungen().size());

    }
}
