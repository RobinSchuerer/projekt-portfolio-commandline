package de.lv1871.projektportfolio.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.lv1871.projektportfolio.domain.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ServiceTest {

    private ProjektPortfolioVorschlagService service = new ProjektPortfolioVorschlagService();
    private Team team;
    private ProjektPortfolioEingabeDaten eingabeDaten;

    @Before
    public void setUp() throws Exception {
        team = Team.newBuilder().withName("team1").build();

        eingabeDaten = ProjektPortfolioEingabeDaten
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
                                .withProjekt(newProjekt("Projekt A", ProjektTyp.MUSS_PROJEKT))
                                .withAufwaende(ImmutableMap.of(team,new BigDecimal("12")))
                                .build(),
                        ProjektAufwand
                                .newBuilder()
                                .withProjekt(newProjekt("Projekt B",ProjektTyp.MUSS_PROJEKT))
                                .withAufwaende(ImmutableMap.of(team,new BigDecimal("8")))
                                .build()
                ))
                .build();

    }

    @Test
    public void base() throws Exception {

        ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);

        assertEquals(2, vorschlag.getAufwandVerteilungen().size());
        assertEquals(new BigDecimal("5.00"), vorschlag.getAufwand("team1", "Projekt A", LocalDate.parse("2016-02-01")).get());
        assertEquals(new BigDecimal("5.00"), vorschlag.getAufwand("team1", "Projekt B", LocalDate.parse("2016-02-01")).get());
        assertEquals(new BigDecimal("3.00"), vorschlag.getAufwand("team1", "Projekt B", LocalDate.parse("2016-01-01")).get());
        assertEquals(new BigDecimal("7.00"), vorschlag.getAufwand("team1", "Projekt A", LocalDate.parse("2016-01-01")).get());
    }

    @Test
    @Ignore("overflow impl")
    public void restriction() throws Exception {
        eingabeDaten.getBeschraenkungen().add(Beschraenkung
                .newBuilder()
                .withTyp(ProjektTyp.MUSS_PROJEKT)
                .withValue(new BigDecimal(0.5))
                .build());

        ProjektPortfolioVorschlag vorschlag = service.berechne(eingabeDaten);

        assertEquals(2, vorschlag.getAufwandVerteilungen().size());
        assertEquals(new BigDecimal("2.50"), vorschlag.getAufwand("team1", "Projekt A", LocalDate.parse("2016-02-01")).get());
        assertEquals(new BigDecimal("2.50"), vorschlag.getAufwand("team1", "Projekt B", LocalDate.parse("2016-02-01")).get());
        assertEquals(new BigDecimal("2.50"), vorschlag.getAufwand("team1", "Projekt B", LocalDate.parse("2016-01-01")).get());
        assertEquals(new BigDecimal("2.50"), vorschlag.getAufwand("team1", "Projekt A", LocalDate.parse("2016-01-01")).get());

    }

    private Projekt newProjekt(@Nonnull String name,
                               @Nonnull ProjektTyp typ) {

        return Projekt.newBuilder().withName(name).withTyp(typ).withDeadLine(LocalDate.parse("2016-02-01")).build();
    }
}
