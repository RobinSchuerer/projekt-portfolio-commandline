package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.ProjektAufwand;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;

import javax.annotation.Nonnull;

public class EingabeDatenValidator {


    public ValidationErrors validate(@Nonnull ProjektPortfolioEingabeDaten eingabeDaten) {
        ValidationErrors validationErrors = new ValidationErrors();
        eingabeDaten.getProjektAufwaende()
                .stream()
                .filter(ProjektAufwand.MUSS_PROJEKT_FILTER.or(ProjektAufwand.PRODUKT_PROJEKT_FILTER))
                .filter(projektAufwand -> projektAufwand.getDeadLine() == null)
                .forEach(projektAufwand ->
                        validationErrors.add("Keine Deadline für: " + projektAufwand.getProjekt().getName() +" "+'\n'));

        return validationErrors;
    }
}
