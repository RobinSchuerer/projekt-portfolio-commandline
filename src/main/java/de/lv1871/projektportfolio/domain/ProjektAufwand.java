package de.lv1871.projektportfolio.domain;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjektAufwand {

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

    public LocalDate getDeadLine(){
        return getProjekt().getDeadLine();
    }

    @Override
    public String toString() {
        return "ProjektAufwand{" +
                "projekt=" + projekt +
                ", aufwaende=" + aufwaende +
                '}';
    }
}

