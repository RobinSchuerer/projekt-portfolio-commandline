package de.lv1871.projektportfolio.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.lv1871.projektportfolio.domain.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GleichverteilungMitFolgeCheckTest {

    private static final Projekt MUSS_PROJEKT = Projekt
            .newBuilder()
            .withName("Test1")
            .withTyp(ProjektTyp.MUSS_PROJEKT)
            .withDeadLine(LocalDate.parse("2016-12-01"))
            .build();

    private static final Projekt PRODUKT_PROJEKT = Projekt
            .newBuilder()
            .withName("Test2")
            .withTyp(ProjektTyp.PRODUKT_PROJEKT)
            .withDeadLine(LocalDate.parse("2016-12-01"))
            .build();

    private static final Team TEAM = Team.newBuilder().withName("team").build();

    private GleichverteilungMitFolgeCheck toTest = new GleichverteilungMitFolgeCheck();

    @Test
    public void fall01() {
//        Anderes Beispiel:
//        80% Muss Projekte
//        30% Produkt Projekte
//        10 PT
//
//                ==> 5 PT Muss und 5 PT Produkt verletzt die 30%-Bedingung bei Produkt-Projekten
//                ==> nur 3 PT für das Produktprojekt und 7 PT für das Muss-Projekt
//                 (==> beide Bedingungen erfüllt)


        ProjektPortfolioEingabeDaten eingabeDaten = ProjektPortfolioEingabeDaten
                .newBuilder()
                .withProjektAufwaende(Lists.newArrayList(
                        ProjektAufwand
                                .newBuilder()
                                .withProjekt(MUSS_PROJEKT)
                                .withAufwaende(ImmutableMap.of(TEAM, BigDecimal.TEN))
                                .build(),
                        ProjektAufwand
                                .newBuilder()
                                .withProjekt(PRODUKT_PROJEKT)
                                .withAufwaende(ImmutableMap.of(TEAM, BigDecimal.TEN))
                                .build()))
                .withTeamKapazitaeten(Lists.newArrayList(
                        TeamKapazitaet
                                .newBuilder()
                                .withTeam(TEAM)
                                .withMonat(LocalDate.parse("2016-12-01"))
                                .withKapazitaet(BigDecimal.TEN)
                                .build(),
                        TeamKapazitaet
                                .newBuilder()
                                .withTeam(TEAM)
                                .withMonat(LocalDate.parse("2016-11-01"))
                                .withKapazitaet(BigDecimal.TEN)
                                .build()))
                .withBeschraenkungen(Lists.newArrayList(
                        Beschraenkung
                                .newBuilder()
                                .withTyp(ProjektTyp.MUSS_PROJEKT)
                                .withValue(new BigDecimal("0.80"))
                                .build(),
                        Beschraenkung
                                .newBuilder()
                                .withTyp(ProjektTyp.PRODUKT_PROJEKT)
                                .withValue(new BigDecimal("0.30"))
                                .build()
                ))
                .build();


    }
}
