package de.lv1871.projektportfolio.service;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import de.lv1871.projektportfolio.domain.ProjektAufwand;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        LocalDate startMonat = eingabeDaten.getStartMonat();

        for (ProjektAufwand projektAufwand : projekte) {
            BigDecimal rest = projektAufwand.getAufwand(team);

            for(int i = 0; i <Integer.MAX_VALUE; i++){
                
                // aktueller monat
                LocalDate monat = startMonat.plusMonths(i);
                if(eingabeDaten.isAusserhalbZeitraum(monat)){
                    break;
                }

                BigDecimal kapazitaet = getKapazitaet(eingabeDaten,team,pflichtProjekteVorschlag,monat);
                
                BigDecimal aufwand = rest.min(kapazitaet);
                
                pflichtProjekteVorschlag.add(team,projektAufwand.getProjekt(),monat,aufwand);

                rest = rest.subtract(aufwand);

                if(rest.doubleValue() == 0){
                    pflichtProjekteVorschlag.addDeadLine(team, projektAufwand.getProjekt(), monat);

                    break;
                }
            }

            if(rest.doubleValue() > 0){
                pflichtProjekteVorschlag.addUeberlauf(team,projektAufwand.getProjekt(),rest);
            }
        }

        return pflichtProjekteVorschlag;
    }

    private BigDecimal getKapazitaet(@Nonnull ProjektPortfolioEingabeDaten eingabeDaten,
                                     @Nonnull Team team,
                                     @Nonnull ProjektPortfolioVorschlag vorschlag,
                                     @Nonnull LocalDate monat) {

        Optional<BigDecimal> kapazitaet = eingabeDaten.getKapazitaet(team, monat);
        if(!kapazitaet.isPresent()){
            return BigDecimal.ZERO;
        }

        BigDecimal verbraucht = vorschlag.getAufwand(team,monat).orElse(BigDecimal.ZERO);
        
        return kapazitaet.get().subtract(verbraucht).max(BigDecimal.ZERO);

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
