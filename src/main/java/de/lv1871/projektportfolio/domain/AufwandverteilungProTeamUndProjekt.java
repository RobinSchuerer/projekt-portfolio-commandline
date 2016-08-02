package de.lv1871.projektportfolio.domain;

import javax.annotation.Nonnull;
import java.util.List;

public class AufwandverteilungProTeamUndProjekt {

    private Team team;
    private Projekt projekt;

    private List<AufwandProMonat> aufwaende;

    private AufwandverteilungProTeamUndProjekt(Builder builder) {
        team = builder.team;
        projekt = builder.projekt;
        aufwaende = builder.aufwaende;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Team getTeam() {
        return team;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public List<AufwandProMonat> getAufwaende() {
        return aufwaende;
    }


    public static final class Builder {
        private Team team;
        private Projekt projekt;
        private List<AufwandProMonat> aufwaende;

        private Builder() {
        }

        @Nonnull
        public Builder withTeam(@Nonnull Team val) {
            team = val;
            return this;
        }

        @Nonnull
        public Builder withProjekt(@Nonnull Projekt val) {
            projekt = val;
            return this;
        }

        @Nonnull
        public Builder withAufwaende(@Nonnull List<AufwandProMonat> val) {
            aufwaende = val;
            return this;
        }

        @Nonnull
        public AufwandverteilungProTeamUndProjekt build() {
            return new AufwandverteilungProTeamUndProjekt(this);
        }
    }
}
