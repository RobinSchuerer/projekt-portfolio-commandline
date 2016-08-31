package de.lv1871.projektportfolio.service;

import com.google.common.base.Preconditions;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;
import de.lv1871.projektportfolio.domain.TeamKapazitaet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDate;

@Service
public class ProjektPortfolioVorschlagService {

    @Autowired
    private PflichtProjektStrategy pflichtProjektStrategy;

    @Autowired
    private StrategischeProjekteStrategy strategischeProjekteStrategy;

    @Autowired
    private EingabeDatenValidator validator ;

    public ProjektPortfolioVorschlagService(){

    }

    private ProjektPortfolioVorschlagService(Builder builder) {
        pflichtProjektStrategy = builder.pflichtProjektStrategy;
        strategischeProjekteStrategy = builder.strategischeProjekteStrategy;
        validator = builder.validator;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Nonnull
    public ProjektPortfolioVorschlag berechne(@Nonnull ProjektPortfolioEingabeDaten eingabeDaten) {

        Preconditions.checkNotNull(eingabeDaten);

        ValidationErrors validationErrors = validator.validate(eingabeDaten);

        if(!validationErrors.getFehlerList().isEmpty()){
            throw new RuntimeException(validationErrors.getFehlerList().stream().reduce(String::concat).get());
        }

        ProjektPortfolioVorschlag vorschlag = ProjektPortfolioVorschlag.newBuilder().build();

        // Teamweise Verarbeitung
        for (Team team : eingabeDaten.getTeams()) {

            ProjektPortfolioVorschlag pflichtProjekteVorschlag = pflichtProjektStrategy.verarbeite(eingabeDaten,team);

            ProjektPortfolioVorschlag teamProjektVorschlag =
                    strategischeProjekteStrategy.verarbeite(pflichtProjekteVorschlag,eingabeDaten,team);

            vorschlag.addTeamVorschlag(teamProjektVorschlag);
        }

        // TODO: 31.08.16
        for (TeamKapazitaet teamKapazitaet : eingabeDaten.getTeamKapazitaeten()) {
            LocalDate monat = teamKapazitaet.getMonat();

            vorschlag.add(monat);
        }

        return vorschlag;
    }


    public static final class Builder {

        private PflichtProjektStrategy pflichtProjektStrategy;
        private StrategischeProjekteStrategy strategischeProjekteStrategy;
        private EingabeDatenValidator validator = new EingabeDatenValidator();

        private Builder() {
        }

        @Nonnull
        public Builder withPflichtProjektStrategy(@Nonnull PflichtProjektStrategy val) {
            pflichtProjektStrategy = val;
            return this;
        }

        @Nonnull
        public Builder withValidator(@Nonnull EingabeDatenValidator val) {
            validator = val;
            return this;
        }

        @Nonnull
        public Builder withStrategischeProjekteStrategy(@Nonnull StrategischeProjekteStrategy val) {
            strategischeProjekteStrategy = val;
            return this;
        }

        @Nonnull
        public ProjektPortfolioVorschlagService build() {
            return new ProjektPortfolioVorschlagService(this);
        }
    }
}