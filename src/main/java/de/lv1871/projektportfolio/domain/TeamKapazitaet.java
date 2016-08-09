package de.lv1871.projektportfolio.domain;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TeamKapazitaet {

    private LocalDate monat;
    private Team team;
    private BigDecimal kapazitaet;

    private TeamKapazitaet(Builder builder) {
        setMonat(builder.monat);
        setTeam(builder.team);
        setKapazitaet(builder.kapazitaet);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public LocalDate getMonat() {
        return monat;
    }

    private void setMonat(LocalDate monat) {
        this.monat = monat;
    }

    public Team getTeam() {
        return team;
    }

    private void setTeam(Team team) {
        this.team = team;
    }

    public BigDecimal getKapazitaet() {
        return kapazitaet.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void setKapazitaet(BigDecimal kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public static final class Builder {
        private LocalDate monat;
        private Team team;
        private BigDecimal kapazitaet;

        private Builder() {
        }

        public Builder withMonat(LocalDate val) {
            monat = val;
            return this;
        }

        public Builder withTeam(Team val) {
            team = val;
            return this;
        }

        public Builder withKapazitaet(BigDecimal val) {
            kapazitaet = val;
            return this;
        }

        public TeamKapazitaet build() {
            return new TeamKapazitaet(this);
        }
    }

    @Override
    public String toString() {
        return  team.getName() +": " + monat.format(DateTimeFormatter.ofPattern("MMM YY")) +" - "+getKapazitaet();
    }
}
