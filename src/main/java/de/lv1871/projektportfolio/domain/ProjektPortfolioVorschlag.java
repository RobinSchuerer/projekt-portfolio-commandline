package de.lv1871.projektportfolio.domain;

import javax.annotation.Nonnull;
import java.util.List;

public class ProjektPortfolioVorschlag {

    private final List<AufwandverteilungProTeamUndProjekt> aufwandVerteilungen;

    private ProjektPortfolioVorschlag(Builder builder) {
        aufwandVerteilungen = builder.aufwandVerteilungen;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<AufwandverteilungProTeamUndProjekt> getAufwandVerteilungen() {
        return aufwandVerteilungen;
    }

    public static final class Builder {
        private List<AufwandverteilungProTeamUndProjekt> aufwandVerteilungen;

        private Builder() {
        }

        @Nonnull
        public Builder withAufwandVerteilungen(@Nonnull List<AufwandverteilungProTeamUndProjekt> val) {
            aufwandVerteilungen = val;
            return this;
        }

        @Nonnull
        public ProjektPortfolioVorschlag build() {
            return new ProjektPortfolioVorschlag(this);
        }
    }
}
