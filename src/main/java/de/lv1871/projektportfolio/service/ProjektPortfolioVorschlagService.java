package de.lv1871.projektportfolio.service;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjektPortfolioVorschlagService {

    private static final Predicate<ProjektAufwand> MUSSPROJEKTE_FILTER =
            pa -> pa.getTyp() == ProjektTyp.MUSS_PROJEKT;
    private static final Comparator<ProjektAufwand> SORTIERT_NACH_DEADLINE_ABSTEIGEND =
            (pa1, pa2) -> ComparisonChain
                    .start()
                    .compare(pa1.getDeadLine(), pa2.getDeadLine(), Ordering.natural().reverse().nullsFirst())
                    .result();

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
                    .sorted(SORTIERT_NACH_DEADLINE_ABSTEIGEND)
                    .collect(Collectors.toList());

            if (!mussProjekte.isEmpty()) {
                return verarbeiteMussProjekte(portfolio, team, mussProjekte);
            }
        }
        return ProjektPortfolioVorschlag.newBuilder().build();
    }

    private ProjektPortfolioVorschlag verarbeiteMussProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                             @Nonnull Team team,
                                                             @Nonnull List<ProjektAufwand> mussProjekte) {

        ProjektPortfolioVorschlag result = ProjektPortfolioVorschlag.newBuilder().build();

        // letzte Deadline von hier wird rückwärts aufgeteilt
        LocalDate deadLine = mussProjekte.stream().findFirst().get().getProjekt().getDeadLine();

        Map<Projekt, BigDecimal> todoMap = mussProjekte
                .stream()
                .collect(Collectors.toMap(
                        ProjektAufwand::getProjekt,
                        projektAufwand -> projektAufwand.getAufwand(team)));

        // für jeden monat
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (nichtsZuTun(todoMap)) {
                break;
            }

            LocalDate aktuellerMonat = deadLine.minusMonths(i);

            // Gesamtaufwand für diesen Monat
            Optional<BigDecimal> gesamtOptional = portfolioEingabeDaten
                    .getKapazitaet(team, aktuellerMonat, ProjektTyp.MUSS_PROJEKT);

            if (!gesamtOptional.isPresent()) {
                // overflow
                todoMap.entrySet().forEach(e -> result.addUeberlauf(team, e.getKey(),e.getValue()));
                break;
            }

            BigDecimal gesamt = gesamtOptional.get();

            // welche Projekte müssen in diesem Monat bearbeitet werden
            List<Projekt> relevanteProjekte = getRelevanteProjekte(mussProjekte, aktuellerMonat, todoMap, team);
            for (int i1 = 0; i1 < relevanteProjekte.size(); i1++) {

                Projekt projekt = relevanteProjekte.get(i1);

                BigDecimal anzahl = new BigDecimal(relevanteProjekte.size() - i1);
                BigDecimal proProjekt = gesamt.divide(anzahl, MathContext.DECIMAL32);

                BigDecimal todo = todoMap.get(projekt);
                BigDecimal monatsWert = proProjekt.min(todo);

                gesamt = gesamt.subtract(monatsWert);
                BigDecimal rest = todo.subtract(monatsWert);

                todoMap.put(projekt, rest);

                result.add(team, projekt, aktuellerMonat, monatsWert);
            }
        }

        return result;
    }

    private boolean nichtsZuTun(Map<Projekt, BigDecimal> todoMap) {

        return todoMap.values().stream().reduce(BigDecimal::add).get().doubleValue() <= 0;
    }

    private List<Projekt> getRelevanteProjekte(@Nonnull List<ProjektAufwand> mussProjekte,
                                               @Nonnull LocalDate aktuellerMonat,
                                               @Nonnull Map<Projekt, BigDecimal> todoMap,
                                               @Nonnull Team team) {

        List<Projekt> relevanteProjekte = mussProjekte
                .stream()
                .filter(projektAufwand -> !aktuellerMonat.isAfter(projektAufwand.getDeadLine()))
                .filter(projektAufwand -> todoMap.get(projektAufwand.getProjekt()).doubleValue() > 0)
                .map(ProjektAufwand::getProjekt)
                .sorted((o1, o2) -> todoMap.get(o1).compareTo(todoMap.get(o2)))
                .collect(Collectors.toList());

        return relevanteProjekte;
    }

}