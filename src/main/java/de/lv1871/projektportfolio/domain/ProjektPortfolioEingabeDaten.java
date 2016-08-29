package de.lv1871.projektportfolio.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
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

    public Set<Team> getTeams() {
        return teamKapazitaeten.stream().map(TeamKapazitaet::getTeam).collect(Collectors.toSet());
    }

    public List<TeamKapazitaet> getTeamKapazitaeten() {
        return teamKapazitaeten;
    }

    public List<ProjektAufwand> getProjektAufwaende() {
        return projektAufwaende;
    }

    public Optional<BigDecimal> getKapazitaet(Team team, LocalDate monat, ProjektTyp projektTyp) {
        Optional<TeamKapazitaet> kapazitaetOptional = teamKapazitaeten
                .stream()
                .filter(tk -> tk.getTeam().equals(team))
                .filter(tk -> tk.getMonat().equals(monat))
                .findFirst();

        if (!kapazitaetOptional.isPresent()) {
            return Optional.empty();
        }

        BigDecimal faktor = getFaktor(projektTyp);

        return Optional.of(faktor.multiply(kapazitaetOptional.get().getKapazitaet()));

    }

    private BigDecimal getFaktor(ProjektTyp projektTyp) {

        if (beschraenkungen == null) {
            return BigDecimal.ONE;
        }

        Optional<Beschraenkung> beschraenkung = beschraenkungen.stream().filter(b -> b.getTyp() == projektTyp).findFirst();

        if (!beschraenkung.isPresent()) {
            return BigDecimal.ONE;
        }

        return beschraenkung.get().getValue();
    }

    @Nonnull
    public Optional<BigDecimal> getKapazitaet(@Nonnull Team team, @Nonnull LocalDate aktuellerMonat) {
        Preconditions.checkNotNull(team);
        Preconditions.checkNotNull(aktuellerMonat);

        Optional<TeamKapazitaet> kapazitaetOptional = teamKapazitaeten.stream()
                .filter(TeamKapazitaet.filterTeam(team))
                .filter(TeamKapazitaet.filterMonat(aktuellerMonat))
                .findAny();

        if(!kapazitaetOptional.isPresent()){
            return Optional.empty();
        }

        return Optional.of(kapazitaetOptional.get().getKapazitaet());
    }

    @Nonnull
    public Optional<BigDecimal> getBeschraenkung(@Nonnull ProjektTyp typ) {
        Optional<Beschraenkung> first = beschraenkungen
                .stream()
                .filter(beschraenkung -> beschraenkung.getTyp() == typ).findFirst();


        if (!first.isPresent()) {
            return Optional.of(BigDecimal.ONE);
        }

        return Optional.of(first.get().getValue());
    }

    public boolean isAusserhalbZeitraum(@Nonnull LocalDate aktuellerMonat) {
        Preconditions.checkNotNull(aktuellerMonat);

        LocalDate fruehsterMonat = getTeamKapazitaeten()
                .stream()
                .map(teamKapazitaet -> teamKapazitaet.getMonat())
                .min(Ordering.natural())
                .get();

        return aktuellerMonat.isBefore(fruehsterMonat);
    }

    public LocalDate getStartMonat() {
        // TODO: 29.08.2016  Ermittlung des Startmonats

        return null;
    }

    public static final class Builder {
        
        private String name;
        private List<Beschraenkung> beschraenkungen = Lists.newArrayList();
        private List<TeamKapazitaet> teamKapazitaeten = Lists.newArrayList();
        private List<ProjektAufwand> projektAufwaende = Lists.newArrayList();

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

                    (t1, t2) -> {
                        int nameComparison = t1.getTeam().getName().compareTo(t2.getTeam().getName());
                        if (nameComparison == 0) return t1.getMonat().compareTo(t2.getMonat());

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



    @Nonnull
    public List<ProjektAufwand> getProjektAufwaende(@Nonnull Predicate<? super ProjektAufwand> filter,
                                                    @Nonnull Comparator<? super ProjektAufwand> sortierung) {
        return projektAufwaende
                .stream()
                .filter(Preconditions.checkNotNull(filter))
                .sorted(Preconditions.checkNotNull(sortierung))
                .collect(Collectors.toList());
    }
}
