package de.lv1871.projektportfolio.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.ros.tagmap.TagMap;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjektPortfolioVorschlag {

    private final List<AufwandverteilungProTeamUndProjekt> aufwandVerteilungen;

    private final TagMap<BigDecimal> ueberlauf = new TagMap<>();

    private final TagMap<LocalDate> deadlines = new TagMap<>();

    private ProjektPortfolioVorschlag(Builder builder) {
        aufwandVerteilungen = builder.aufwandVerteilungen;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<AufwandverteilungProTeamUndProjekt> getAufwandVerteilungen() {
        return aufwandVerteilungen;
    }

    @Nonnull
    public ProjektPortfolioVorschlag add(@Nonnull Team team,
                                         @Nonnull Projekt projekt,
                                         @Nonnull LocalDate aktuellerMonat,
                                         @Nonnull BigDecimal monatsWert) {

        AufwandProMonat aufwandProMonat = AufwandProMonat
                .newBuilder()
                .withAufwand(monatsWert)
                .withMonat(aktuellerMonat)
                .build();

        Optional<AufwandverteilungProTeamUndProjekt> proTeamUndProjektOptional = aufwandVerteilungen
                .stream()
                .filter(newTeamFilter(team))
                .filter(newProjektFilter(projekt))
                .findAny();

        if (proTeamUndProjektOptional.isPresent()) {
            proTeamUndProjektOptional.get().getAufwaende().add(aufwandProMonat);
        } else {
            aufwandVerteilungen.add(
                    AufwandverteilungProTeamUndProjekt
                            .newBuilder()
                            .withProjekt(projekt)
                            .withTeam(team)
                            .withAufwaende(Lists.newArrayList(aufwandProMonat))
                            .build()
            );
        }

        return this;
    }

    private static Predicate<? super AufwandverteilungProTeamUndProjekt> newProjektFilter(Projekt projekt) {
        return aufwandverteilungProTeamUndProjekt -> aufwandverteilungProTeamUndProjekt.getProjekt().equals(projekt);
    }

    private static Predicate<? super AufwandverteilungProTeamUndProjekt> newTeamFilter(Team team) {
        return a -> a.getTeam().equals(team);
    }

    @Nonnull
    public Optional<BigDecimal> getAufwand(String teamName, String projektName, LocalDate monat) {
        Optional<AufwandverteilungProTeamUndProjekt> matching = aufwandVerteilungen
                .stream()
                .filter(aufwand -> aufwand.getProjekt().getName().equals(projektName))
                .filter(aufwand -> aufwand.getTeam().getName().equals(teamName))
                .findAny();

        if (!matching.isPresent()) {
            return Optional.empty();
        }

        return matching.get().getAufwaende()
                .stream()
                .filter(aufwandProMonat -> aufwandProMonat.getMonat().equals(monat))
                .map(AufwandProMonat::getAufwand)
                .findAny();
    }

    @Nonnull
    public Optional<BigDecimal> getAufwand(@Nonnull Team team,
                                           @Nonnull LocalDate monat) {

        List<AufwandverteilungProTeamUndProjekt> matching = aufwandVerteilungen
                .stream()
                .filter(aufwand -> aufwand.getTeam().equals(team))
                .collect(Collectors.toList());


        return matching
                .stream()
                .map(proTeamUndProjekt -> proTeamUndProjekt.getAufwaende()
                        .stream()
                        .filter(proMonat -> proMonat.getMonat().equals(monat))
                        .map(proMonat -> proMonat.getAufwand())
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal::add);
    }

    @Nonnull
    public Optional<BigDecimal> getUeberlauf(@Nonnull String teamName, @Nonnull String projektName) {

        List<BigDecimal> values = ueberlauf.getValues(
                Team.newBuilder().withName(teamName).build(),
                Projekt.newBuilder().withName(projektName).build());

        if (values.isEmpty()) {
            return Optional.empty();
        }

        return normalize(Optional.of(values.get(0)));
    }

    @Nonnull
    public Optional<BigDecimal> getUeberlauf(@Nonnull String teamName) {
        Optional<BigDecimal> reduce = ueberlauf.getValues(Team.newBuilder().withName(teamName).build())
                .stream()
                .reduce(BigDecimal::add);
        return normalize(reduce);
    }

    private Optional<BigDecimal> normalize(Optional<BigDecimal> reduce) {
        if (!reduce.isPresent()) {
            return Optional.empty();
        }

        if (reduce.isPresent() && reduce.get().doubleValue() == 0d) {
            return Optional.empty();
        }

        return Optional.of(reduce.get().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Nonnull
    public Set<String> getTeams() {
        return this.aufwandVerteilungen
                .stream()
                .map(AufwandverteilungProTeamUndProjekt::getTeam)
                .map(Team::getName)
                .collect(Collectors.toSet());
    }

    @Nonnull
    public Set<String> getProjekte() {
        return this.aufwandVerteilungen
                .stream()
                .map(AufwandverteilungProTeamUndProjekt::getProjekt)
                .map(Projekt::getName)
                .collect(Collectors.toSet());
    }

    @Nonnull
    public ProjektPortfolioVorschlag addUeberlauf(Team team, Projekt projekt, BigDecimal value) {
        this.ueberlauf.put(value.setScale(2), team, projekt);

        return this;
    }

    @Nonnull
    public ProjektPortfolioVorschlag addTeamVorschlag(@Nonnull ProjektPortfolioVorschlag teamProjektVorschlag) {
        Preconditions.checkNotNull(teamProjektVorschlag);

        aufwandVerteilungen.addAll(teamProjektVorschlag.getAufwandVerteilungen());
        ueberlauf.merge(teamProjektVorschlag.ueberlauf);
        deadlines.merge(teamProjektVorschlag.deadlines);

        return this;
    }

    @Nonnull
    public ProjektPortfolioVorschlag addUeberlauf(@Nonnull String teamName, @Nonnull BigDecimal overflow) {
        this.ueberlauf.put(overflow, Team.newBuilder().withName(teamName).build());

        return this;
    }

    @Nonnull
    public ProjektPortfolioVorschlag addDeadLine(@Nonnull Team team,
                                                 @Nonnull Projekt projekt,
                                                 @Nonnull LocalDate monat) {
        this.deadlines.put(monat, team, projekt);

        return this;
    }

    @Nonnull
    public Optional<LocalDate> getDealine(@Nonnull String projektName) {
        List<LocalDate> values = deadlines.getValues(Projekt.newBuilder().withName(projektName).build());

        return values.stream().max(Ordering.natural());
    }

    @Nonnull
    public ProjektPortfolioVorschlag addDeadLine(@Nonnull String projektName, @Nonnull LocalDate deadline) {
        deadlines.put(deadline,Projekt.newBuilder().withName(projektName).build());
        return this;
    }

    @Nonnull
    public ProjektPortfolioVorschlag updateDeadlinesFuerPflichtProjekte() {
      asfassdfasdfd




    }

    public static final class Builder {

        private List<AufwandverteilungProTeamUndProjekt> aufwandVerteilungen = Lists.newArrayList();

        private Builder() {
        }

        @Nonnull
        public Builder withAufwandVerteilungen(@Nonnull List<AufwandverteilungProTeamUndProjekt> val) {
            aufwandVerteilungen.addAll(val);
            return this;
        }

        @Nonnull
        public ProjektPortfolioVorschlag build() {
            return new ProjektPortfolioVorschlag(this);
        }
    }
}
