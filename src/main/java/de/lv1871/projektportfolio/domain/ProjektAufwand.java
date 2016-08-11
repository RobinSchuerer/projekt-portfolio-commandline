package de.lv1871.projektportfolio.domain;


import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ProjektAufwand {

    public static final Predicate<ProjektAufwand> MUSS_PROJEKT_FILTER =
            projektTypFilter(ProjektTyp.MUSS_PROJEKT);

    public static final Predicate<ProjektAufwand> PRODUKT_PROJEKT_FILTER =
            projektTypFilter(ProjektTyp.PRODUKT_PROJEKT);

    public static final Predicate<ProjektAufwand> STRATEGISCHES_PROJEKT_FILTER =
            projektTypFilter(ProjektTyp.STRATEGISCHES_PROJEKT);

    public static final Comparator<ProjektAufwand> SORTIERT_NACH_DEADLINE_ABSTEIGEND =
            (pa1, pa2) -> ComparisonChain
                    .start()
                    .compare(pa1.getDeadLine(), pa2.getDeadLine(), Ordering.natural().reverse().nullsFirst())
                    .result();

    private Projekt projekt;
    private Map<Team, BigDecimal> aufwaende = new HashMap<>();

    private ProjektAufwand(Builder builder) {
        setProjekt(builder.projekt);
        setAufwaende(builder.aufwaende);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Projekt getProjekt() {
        return projekt;
    }

    private void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public Map<Team, BigDecimal> getAufwaende() {
        return aufwaende;
    }

    private void setAufwaende(Map<Team, BigDecimal> aufwaende) {
        this.aufwaende = aufwaende;
    }

    public BigDecimal getAufwand(Team team) {
        return aufwaende.get(team);
    }

    public ProjektTyp getTyp() {
        return getProjekt().getTyp();
    }


    public static final class Builder {
        private Projekt projekt;
        private Map<Team, BigDecimal> aufwaende;

        private Builder() {
        }

        public Builder withProjekt(Projekt val) {
            projekt = val;
            return this;
        }

        public Builder withAufwaende(Map<Team, BigDecimal> val) {
            aufwaende = val;
            return this;
        }

        public ProjektAufwand build() {
            return new ProjektAufwand(this);
        }
    }

    public LocalDate getDeadLine() {
        return getProjekt().getDeadLine();
    }

    @Override
    public String toString() {
        return "ProjektAufwand{" +
                "projekt=" + projekt +
                ", aufwaende=" + aufwaende +
                '}';
    }

    public static Predicate<ProjektAufwand> teamFilter(@Nonnull Team team) {
        Preconditions.checkNotNull(team);

        return (projektAufwand) -> projektAufwand.getAufwaende().keySet().contains(team);
    }

    public static final Predicate<ProjektAufwand> projektTypFilter(@Nonnull ProjektTyp typ) {
        Preconditions.checkNotNull(typ);

        return projektAufwand -> projektAufwand.getTyp() == typ;
    }

}

