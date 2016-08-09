package de.lv1871.projektportfolio.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjektPortfolioEingabeDaten {

    private final String name;
    private final List<Beschraenkung> beschraenkungen;
    private final List<TeamKapazitaet> teamKapazitaeten;
    private final List<ProjektAufwand> projektAufwaende;

    private ProjektPortfolioEingabeDaten(Builder builder) {
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
        return teamKapazitaeten.stream().map(TeamKapazitaet::getTeam).collect(Collectors.toSet());
    }

    public List<TeamKapazitaet> getTeamKapazitaeten() {
        return teamKapazitaeten;
    }

    public List<ProjektAufwand> getProjektAufwaende() {
        return projektAufwaende;
    }

    public BigDecimal getKapazitaet(Team team, LocalDate monat, ProjektTyp projektTyp) {
        TeamKapazitaet kapazitaet = teamKapazitaeten
                .stream()
                .filter(tk -> tk.getTeam().equals(team))
                .filter(tk -> tk.getMonat().equals(monat))
                .findFirst()
                .get();

        BigDecimal faktor = getFaktor(projektTyp);

        return faktor.multiply(kapazitaet.getKapazitaet());

    }

    private BigDecimal getFaktor(ProjektTyp projektTyp) {

        Optional<Beschraenkung> beschraenkung = beschraenkungen.stream().filter(b -> b.getTyp() == projektTyp).findFirst();

        if(!beschraenkung.isPresent()){
            return BigDecimal.ONE;
        }

        return beschraenkung.get().getValue();
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

        public ProjektPortfolioEingabeDaten build() {
            return new ProjektPortfolioEingabeDaten(this);
        }
    }
}
