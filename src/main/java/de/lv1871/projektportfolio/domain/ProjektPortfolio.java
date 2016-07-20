package de.lv1871.projektportfolio.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class ProjektPortfolio {

    private final String name;
    private final List<Beschraenkung> beschraenkungen;
    private final List<TeamKapazitaet> teamKapazitaeten;
    private final List<ProjektAufwand> projektAufwaende;

    private ProjektPortfolio(Builder builder) {
        name = builder.name;
        beschraenkungen = builder.beschraenkungen;
        teamKapazitaeten = builder.teamKapazitaeten;
        projektAufwaende = builder.projektAufwaende;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<Beschraenkung> getBeschraenkungen() {
        return beschraenkungen;
    }

    public Set<Team> getTeams(){
        return teamKapazitaeten.stream().map(k->k.getTeam()).collect(Collectors.toSet());
    }

    public List<TeamKapazitaet> getTeamKapazitaeten() {
        return teamKapazitaeten;
    }

    public List<ProjektAufwand> getProjektAufwaende() {
        return projektAufwaende;
    }

    public static final class Builder {
        private String name;
        private List<Beschraenkung> beschraenkungen;
        private List<TeamKapazitaet> teamKapazitaeten;
        private List<ProjektAufwand> projektAufwaende;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withBeschraenkungen(List<Beschraenkung> val) {
            beschraenkungen = val;
            return this;
        }

        public Builder withTeamKapazitaeten(List<TeamKapazitaet> val) {
            teamKapazitaeten = val.stream().sorted(

                    (t1,t2)-> {
                        int nameComparison = t1.getTeam().getName().compareTo(t2.getTeam().getName());
                        if(nameComparison == 0) return t1.getMonat().compareTo(t2.getMonat());

                        return nameComparison;

                    }).collect(Collectors.toList());
            return this;
        }

        public Builder withProjektAufwaende(List<ProjektAufwand> val) {
            projektAufwaende = val;
            return this;
        }

        public ProjektPortfolio build() {
            return new ProjektPortfolio(this);
        }
    }
}
