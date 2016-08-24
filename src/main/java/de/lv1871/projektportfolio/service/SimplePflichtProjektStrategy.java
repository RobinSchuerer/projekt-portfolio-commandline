package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Simple Implementierung, es wird immer der komplette verfügbare Aufwand entsprechend der Vorgaben durch die Restriktionen
 * auf die Muss und Produkt Projekte verteilt
 */
public class SimplePflichtProjektStrategy implements  PflichtProjektStrategy {


    @Override
    public ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioEingabeDaten eingabeDaten,
                                                @Nonnull Team team) {

        List<ProjektAufwand> mussProjekte = eingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.MUSS_PROJEKT_FILTER),
                ProjektAufwand.SORTIERT_NACH_DEADLINE_ABSTEIGEND
        );

        List<ProjektAufwand> produktProjekte = eingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.PRODUKT_PROJEKT_FILTER),
                ProjektAufwand.SORTIERT_NACH_DEADLINE_ABSTEIGEND
        );

        mussProjekte.addAll(produktProjekte);

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
            Optional<BigDecimal> gesamtOptional = eingabeDaten
                    .getKapazitaet(team, aktuellerMonat, ProjektTyp.MUSS_PROJEKT);

            if (!gesamtOptional.isPresent()) {
                // overflow
                todoMap.entrySet().forEach(e -> result.addUeberlauf(team, e.getKey(),e.getValue()));
                break;
            }

            BigDecimal gesamt = gesamtOptional.get();

            // welche Projekte müssen in diesem Monat bearbeitet werden
            List<Projekt> relevanteProjekte = getRelevanteProjekte(mussProjekte, aktuellerMonat, todoMap);
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
