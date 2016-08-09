package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjektPortfolioVorschlagService {

    private static final Predicate<ProjektAufwand> MUSSPROJEKTE_FILTER =
            pa -> pa.getTyp() == ProjektTyp.MUSS_PROJEKT;
    private static final Comparator<ProjektAufwand> SORTIERT_NACH_DEADLINE =
            (pa1, pa2) -> pa1.getDeadLine().compareTo(pa2.getDeadLine());

    @Nonnull
    public ProjektPortfolioVorschlag berechne(@Nonnull ProjektPortfolioEingabeDaten portfolio) {

        // iteriere über teams
        for (Team team : portfolio.getTeams()) {
            // hole alle Projekte, an denen dieses Team beteiligt ist
            Set<ProjektAufwand> projektAufwaende = portfolio.getProjektAufwaende()
                    .stream()
                    .filter(projektAufwand -> projektAufwand.getAufwaende().keySet().contains(team))
                    .collect(Collectors.toSet());

            // Muss Projekte
            List<ProjektAufwand> mussProjekte = projektAufwaende
                    .stream()
                    .filter(MUSSPROJEKTE_FILTER)
                    .sorted(SORTIERT_NACH_DEADLINE)
                    .collect(Collectors.toList());

            if (!mussProjekte.isEmpty()) {
                verarbeiteMussProjekte(portfolio, team, mussProjekte);
            }
        }
        return ProjektPortfolioVorschlag.newBuilder().build();
    }

    private void verarbeiteMussProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                        @Nonnull Team team,
                                        @Nonnull List<ProjektAufwand> mussProjekte) {

        // letzte Deadline von hier wird rückwärts aufgeteilt
        LocalDate aktuellerMonat = mussProjekte.stream().findFirst().get().getProjekt().getDeadLine();

        Map<Projekt, BigDecimal> todoMap = mussProjekte
                .stream()
                .collect(Collectors.toMap(
                        ProjektAufwand::getProjekt,
                        projektAufwand -> projektAufwand.getAufwand(team)));

        // für jeden monat
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            aktuellerMonat = aktuellerMonat.minusMonths(i);

            // Gesamtaufwand für diesen Monat
            BigDecimal gesamt = portfolioEingabeDaten.getKapazitaet(team, aktuellerMonat, ProjektTyp.MUSS_PROJEKT);

            // welche Projekte müssen in diesem Monat bearbeitet werden
            List<Projekt> relevanteProjekte = getRelevanteProjekte(mussProjekte,aktuellerMonat, todoMap,team);
            for (int i1 = 0; i1 < mussProjekte.size(); i1++) {

                ProjektAufwand projektAufwand = mussProjekte.get(i1);
                BigDecimal anzahl = new BigDecimal(mussProjekte.size() - i1);
                BigDecimal proProjekt = gesamt.divide(anzahl, MathContext.DECIMAL32);

                BigDecimal todo = todoMap.get(projektAufwand);
                BigDecimal monatsWert = proProjekt.min(todo);

                todoMap.put(projektAufwand.getProjekt(),todo.subtract(monatsWert));

                System.out.println(projektAufwand + " " + aktuellerMonat + " " + monatsWert);
            }

        }

    }

    private List<Projekt> getRelevanteProjekte(@Nonnull List<ProjektAufwand> mussProjekte,
                                               @Nonnull LocalDate aktuellerMonat,
                                               @Nonnull Map<Projekt, BigDecimal> todoMap,
                                               @Nonnull Team team) {

        List<Projekt> relevanteProjekte = mussProjekte
                .stream()
                .filter(projektAufwand -> !aktuellerMonat.isAfter(projektAufwand.getDeadLine()))
                .filter(projektAufwand -> todoMap.get(projektAufwand.getProjekt()).doubleValue() > 0)
                .sorted((o1, o2) -> o1.getAufwand(team).compareTo(o2.getAufwand(team)))
                .map(ProjektAufwand::getProjekt)
                .collect(Collectors.toList());

        return relevanteProjekte;
    }

}