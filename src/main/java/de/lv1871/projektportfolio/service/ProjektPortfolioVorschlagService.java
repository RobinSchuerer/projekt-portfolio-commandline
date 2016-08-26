package de.lv1871.projektportfolio.service;

import com.google.common.base.Preconditions;
import de.lv1871.projektportfolio.domain.ProjektPortfolioEingabeDaten;
import de.lv1871.projektportfolio.domain.ProjektPortfolioVorschlag;
import de.lv1871.projektportfolio.domain.Team;

import javax.annotation.Nonnull;

public class ProjektPortfolioVorschlagService {

    private PflichtProjektStrategy pflichtProjektStrategy;

    private StrategischeProjekteStrategy strategischeProjekteStrategy;

    private EingabeDatenValidator validator ;

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