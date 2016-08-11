package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;

import javax.annotation.Nonnull;

/**
 * Verarbeitung von Pflicht Projekten (Gesetzlich und Produkte)
 */
public interface PflichtProjektStrategy {

    @Nonnull
    ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                         @Nonnull Team team);
}
