package de.lv1871.projektportfolio.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.lv1871.projektportfolio.domain.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ServiceTest {

    private ProjektPortfolioVorschlagService service = new ProjektPortfolioVorschlagService();

    @Test
    public void base() throws Exception {
        Team team = Team.newBuilder().withName("team1").build();

        ProjektPortfolioEingabeDaten test = ProjektPortfolioEingabeDaten
                .newBuilder()
                .withTeamKapazitaeten(
                        Lists.newArrayList(
                                TeamKapazitaet
                                        .newBuilder()
                                        .withKapazitaet(BigDecimal.TEN)
                                        .withMonat(LocalDate.parse("2016-01-01"))
                                        .withTeam(team)
                                        .build(),
                                TeamKapazitaet
                                        .newBuilder()
                                        .withKapazitaet(BigDecimal.TEN)
                                        .withMonat(LocalDate.parse("2016-02-01"))
                                        .withTeam(team)
                                        .build()
                        ))
                .withProjektAufwaende(Lists.newArrayList(
                        ProjektAufwand
                                .newBuilder()
                                .withProjekt(Projekt.newBuilder().withName("Projekt A").build())
                                .withAufwaende(ImmutableMap.of(team,new BigDecimal("12")))
                                .build(),
                        ProjektAufwand
                                .newBuilder()
                                .withProjekt(Projekt.newBuilder().withName("Projekt B").build())
                                .withAufwaende(ImmutableMap.of(team,new BigDecimal("8")))
                                .build()
                        ))
                .build();

        ProjektPortfolioVorschlag vorschlag = service.berechne(test);

        assertEquals(1, vorschlag.getAufwandVerteilungen().size());

    }
}