package de.lv1871.projektportfolio.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In der ersten Iteration wird alles gleich verteilt. Danach folgt ein Check ob die Restriktionen eingehalten sind.
 * Wenn dieser Check nicht eingehalten wird. So werden die Aufwände entsprechend der #
 */
public class GleichverteilungMitFolgeCheck implements PflichtProjektStrategy {


    @Nonnull
    @Override
    public ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                @Nonnull Team team) {

        List<ProjektAufwand> mussProjekte = getMussProjekte(portfolioEingabeDaten, team);
        List<ProjektAufwand> produktProjekte = getProduktProjekte(portfolioEingabeDaten, team);
        List<ProjektAufwand> alleProjekte = getAlleProjekte(mussProjekte, produktProjekte);

        ProjektPortfolioVorschlag result = ProjektPortfolioVorschlag.newBuilder().build();

        if (alleProjekte.isEmpty()) {
            return result;
        }

        // letzte Deadline von hier wird rückwärts aufgeteilt
        LocalDate deadLine = getSpaetesteDeadline(alleProjekte);

        Map<Projekt, BigDecimal> todoMap = getTodoMap(team, alleProjekte);

        // für jeden monat
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            LocalDate aktuellerMonat = deadLine.minusMonths(i);


        }

        return result;
    }

    private Map<Projekt, BigDecimal> getTodoMap(@Nonnull Team team, List<ProjektAufwand> alleProjekte) {
        return alleProjekte
                .stream()
                .collect(Collectors.toMap(
                        ProjektAufwand::getProjekt,
                        projektAufwand -> projektAufwand.getAufwand(team)));
    }

    private LocalDate getSpaetesteDeadline(List<ProjektAufwand> alleProjekte) {
        return alleProjekte.stream().map(ProjektAufwand::getDeadLine).max(Ordering.natural()).get();
    }

    private List<ProjektAufwand> getAlleProjekte(List<ProjektAufwand> mussProjekte, List<ProjektAufwand> produktProjekte) {
        List<ProjektAufwand> alleProjekte = Lists.newArrayList(mussProjekte);
        alleProjekte.addAll(produktProjekte);
        return alleProjekte;
    }

    private List<ProjektAufwand> getProduktProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                    @Nonnull Team team) {

        return portfolioEingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.PRODUKT_PROJEKT_FILTER),
                Ordering.allEqual());
    }

    private List<ProjektAufwand> getMussProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                 @Nonnull Team team) {

        return portfolioEingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.MUSS_PROJEKT_FILTER),
                Ordering.allEqual());
    }

    private List<Projekt> getRelevanteProjekte(@Nonnull List<ProjektAufwand> mussProjekte,
                                               @Nonnull LocalDate aktuellerMonat,
                                               @Nonnull Map<Projekt, BigDecimal> todoMap) {

        List<Projekt> relevanteProjekte = mussProjekte
                .stream()
                .filter(projektAufwand -> !aktuellerMonat.isAfter(projektAufwand.getDeadLine()))
                .filter(projektAufwand -> todoMap.get(projektAufwand.getProjekt()).doubleValue() > 0)
                .map(ProjektAufwand::getProjekt)
                .sorted((o1, o2) -> todoMap.get(o1).compareTo(todoMap.get(o2)))
                .collect(Collectors.toList());

        return relevanteProjekte;
    }

    private boolean nichtsZuTun(Map<Projekt, BigDecimal> todoMap) {

        return todoMap.values().stream().reduce(BigDecimal::add).get().doubleValue() <= 0;
    }
}