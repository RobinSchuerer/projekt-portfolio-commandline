package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ProjektPortfolioVorschlagService {

    private static final Predicate<ProjektAufwand> MUSSPROJEKTE_FILTER =
            pa -> pa.getProjekt().getTyp() == ProjektTyp.MUSS_PROJEKT;

    @Nonnull
    public ProjektPortfolioVorschlag berechne(@Nonnull ProjektPortfolioEingabeDaten portfolio){
        List<ProjektAufwand> projektAufwaende = portfolio.getProjektAufwaende();

        // jeder Projektwand muss auf die verschiedenen Teams verteilt werden
        // gestartet wird mit den Muss Projekten
        projektAufwaende.stream().filter(MUSSPROJEKTE_FILTER).forEach(
                projektAufwand -> {

                    // wieviel muss jedes Team insgesamt leisten
                    Map<Team, BigDecimal> aufwaende = projektAufwand.getAufwaende();
                    for (Team team : aufwaende.keySet()) {
                        LocalDate deadLine = projektAufwand.getProjekt().getDeadLine();
                        for (int i = 0; i < Integer.MAX_VALUE; i++) {
                            LocalDate monat = deadLine.minusMonths(i);

                            // wieviel muss das team hier leisten
                            BigDecimal zuLeisten = getZuLeisten()


                        }
                    }
                }

        );


        return ProjektPortfolioVorschlag.newBuilder().build();
    }
}
