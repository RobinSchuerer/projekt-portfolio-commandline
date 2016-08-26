package de.lv1871.projektportfolio.service;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import de.lv1871.projektportfolio.domain.ProjektAufwand;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alle strategischen projekte werden nach Priorität abgearbeitet
 */
public class SimpleStrategischeProjekteStrategy implements StrategischeProjekteStrategy {

    @Nonnull
    @Override
    public ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioVorschlag pflichtProjekteVorschlag,
                                                @Nonnull ProjektPortfolioEingabeDaten eingabeDaten,
                                                @Nonnull Team team) {

        List<ProjektAufwand> projekte = getProjekteSortiertNachPrioritaet(eingabeDaten);

        // Start monat


        return null;
    }

    private List<ProjektAufwand> getProjekteSortiertNachPrioritaet(@Nonnull ProjektPortfolioEingabeDaten eingabeDaten) {
        return eingabeDaten.getProjektAufwaende()
                .stream()
                .filter(ProjektAufwand.STRATEGISCHES_PROJEKT_FILTER)
                .sorted((o1, o2) ->
                        ComparisonChain
                                .start()
                                .compare(o1.getProjekt().getPrioritaet(),
                                        o2.getProjekt().getPrioritaet(),
                                        Ordering.natural())
                                .result())
                .collect(Collectors.toList());
    }
}
