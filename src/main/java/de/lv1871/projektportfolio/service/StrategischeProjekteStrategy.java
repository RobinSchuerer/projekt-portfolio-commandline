package de.lv1871.projektportfolio.service;

import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;

import javax.annotation.Nonnull;

/**
 * Verabeitung der Strategischen Projekte auf Basis der bereits verarbeiteten Pflichtprojekte
 */
public interface StrategischeProjekteStrategy {

    @Nonnull
    ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioVorschlag pflichtProjekteVorschlag,
                                         @Nonnull ProjektPortfolioEingabeDaten eingabeDaten,
                                         @Nonnull Team team);
}
